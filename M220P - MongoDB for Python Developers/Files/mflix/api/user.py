from flask import jsonify, Blueprint, make_response, request
from mflix.db import get_user, add_user, login_user, make_admin, \
    logout_user, delete_user, update_prefs, most_active_commenters
from mflix.api.utils import expect
from bson.json_util import dumps, loads

from flask_jwt_extended import (
    jwt_required, create_access_token,
    get_jwt_claims
)

from flask_cors import CORS

from flask import current_app, g
from werkzeug.local import LocalProxy


def get_bcrypt():
    bcrypt = getattr(g, '_bcrypt', None)
    if bcrypt is None:
        bcrypt = g._bcrypt = current_app.config['BCRYPT']
    return bcrypt


def get_jwt():
    jwt = getattr(g, '_jwt', None)
    if jwt is None:
        jwt = g._jwt = current_app.config['JWT']

    return jwt


def init_claims_loader():
    add_claims = getattr(g, '_add_claims', None)
    if add_claims is None:
        add_claims = g._add_claims = current_app.config['ADD_CLAIMS']
    return add_claims


jwt = LocalProxy(get_jwt)
bcrypt = LocalProxy(get_bcrypt)
add_claims_to_access_token = LocalProxy(init_claims_loader)


user_api_v1 = Blueprint('user_api_v1', 'user_api_v1',
                        url_prefix='/api/v1/user')

CORS(user_api_v1)


class User(object):

    def __init__(self, userdata):
        self.email = userdata.get('email')
        self.name = userdata.get('name')
        self.password = userdata.get('password')
        self.preferences = userdata.get('preferences')
        self.is_admin = userdata.get('isAdmin', False)

    def to_json(self):
        return loads(dumps(self, default=lambda o: o.__dict__, sort_keys=True))

    @staticmethod
    def from_claims(claims):
        return User(claims.get('user'))


@user_api_v1.route('/register', methods=['POST'])
def register():
    try:
        post_data = request.get_json()
        email = expect(post_data['email'], str, 'email')
        name = expect(post_data['name'], str, 'name')
        password = expect(post_data['password'], str, 'password')
    except Exception as e:
        return jsonify({'error': str(e)}), 400

    errors = {}
    if len(password) < 8:
        errors['password'] = "Your password must be at least 8 characters."

    if len(name) <= 3:
        errors['name'] = "You must specify a name of at least 3 characters."

    if len(errors.keys()) != 0:
        response_object = {
            'status': 'fail',
            'error': errors
        }
        return jsonify(response_object), 411

    insertionresult = add_user(name, email, bcrypt.generate_password_hash(
        password=password.encode('utf8')).decode("utf-8"))
    if 'error' in insertionresult:
        errors['email'] = insertionresult["error"]

    userdata = get_user(email)

    if not userdata:
        errors['general'] = "Internal error, please try again later."

    if len(errors.keys()) != 0:
        response_object = {
            'error': errors
        }
        return make_response(jsonify(response_object)), 400
    else:

        userdata = {
            "email": userdata['email'],
            "name": userdata['name'],
            "preferences": userdata.get('preferences')
        }

        user = User(userdata)
        jwt = create_access_token(user.to_json())

        try:
            login_user(user.email, jwt)
            response_object = {
                'auth_token': jwt,
                'info': userdata
            }
            return make_response(jsonify(response_object)), 201
        except Exception as e:
            response_object = {
                'error': {'internal': e}
            }
            return make_response(jsonify(response_object)), 500


@user_api_v1.route('/login', methods=['POST'])
def login():
    email = ""
    password = ""
    try:
        post_data = request.get_json()
        email = expect(post_data['email'], str, 'email')
        password = expect(post_data['password'], str, 'email')
    except Exception as e:
        jsonify({'error': str(e)}), 400

    userdata = get_user(email)
    if not userdata:
        response_object = {
            'error': {'email': 'Make sure your email is correct.'}
        }
        return make_response(jsonify(response_object)), 401
    if not bcrypt.check_password_hash(userdata['password'], password):
        response_object = {
            'error': {'password': 'Make sure your password is correct.'}
        }
        return make_response(jsonify(response_object)), 401

    userdata = {
        "email": userdata['email'],
        "name": userdata['name'],
        "preferences": userdata.get('preferences'),
        "isAdmin": userdata.get('isAdmin', False)
    }

    user = User(userdata)
    jwt = create_access_token(user.to_json())

    try:
        login_user(user.email, jwt)
        response_object = {
            'auth_token': jwt,
            'info': userdata,
        }
        return make_response(jsonify(response_object)), 201
    except Exception as e:
        response_object = {
            'error': {'internal': e}
        }
        return make_response(jsonify(response_object)), 500


@user_api_v1.route('/update-preferences', methods=['PUT'])
@jwt_required
def save():
    claims = get_jwt_claims()
    user = User.from_claims(claims)
    body = request.get_json()
    prefs = expect(body.get('preferences'), dict, 'preferences')
    try:
        # get an updated user, remove the password
        update_prefs(user.email, prefs)
        updated_user = User(get_user(user.email))
        del updated_user.password
        updated_jwt = create_access_token(updated_user.to_json())
        # lastly, update the user's session
        response_object = {
            'auth_token': updated_jwt,
            'info': updated_user.to_json(),
        }
        return make_response(jsonify(response_object)), 201
    except Exception as e:
        response_object = {
            'error': {'internal': str(e)}
        }
        return make_response(jsonify(response_object)), 500


@user_api_v1.route('/logout', methods=['POST'])
@jwt_required
def logout():
    claims = get_jwt_claims()
    user = User.from_claims(claims)
    try:
        logout_user(user.email)
        response_object = {
            'status': 'logged out'
        }
        return make_response(jsonify(response_object)), 201
    except Exception as e:
        response_object = {
            'error': {'internal': str(e)}
        }
        return make_response(jsonify(response_object)), 401


@user_api_v1.route('/delete', methods=['DELETE'])
@jwt_required
def delete():
    claims = get_jwt_claims()
    user = User.from_claims(claims)
    try:
        password = expect(request.get_json().get('password'), str, 'password')
        userdata = get_user(user.email)
        if (not user.email == userdata['email'] and not
                bcrypt.check_password_hash(userdata['password'], password)):
            response_object = {
                'error': {'password': 'Make sure your password is correct.'}
            }
            return make_response(jsonify(response_object)), 401
        else:
            delete_user(user.email)
            response_object = {
                'status': 'deleted'
            }
            return make_response(jsonify(response_object)), 201
    except Exception as e:
        response_object = {
            'error': {'internal': str(e)}
        }
        return make_response(jsonify(response_object)), 500


@user_api_v1.route('/admin', methods=['GET'])
@jwt_required
def is_admin():
    claims = get_jwt_claims()
    user = User.from_claims(claims)
    try:
        if check_admin(user):
            return jsonify({'status': 'success'}), 202
        else:
            return jsonify({'status': 'fail'}), 401

    except Exception as e:
        return jsonify({'status': 'fail', 'error': str(e)}), 500


@user_api_v1.route('/comment-report', methods=['GET'])
@jwt_required
def comment_report():
    claims = get_jwt_claims()
    user = User.from_claims(claims)
    try:
        if check_admin(user):
            result = most_active_commenters()
            return jsonify({'report': result}), 200
        else:
            return jsonify({'status': 'fail'}), 401
    except Exception as e:
        return jsonify({'status': 'fail', 'error': str(e)}), 500

# the following api call is strictly for the UI


@user_api_v1.route('/make-admin', methods=['POST'])
def make_admin_user_for_ui_test():
    try:
        post_data = request.get_json()
        email = expect(post_data['email'], str, 'email')
        name = expect(post_data['name'], str, 'name')
        password = expect(post_data['password'], str, 'password')
    except Exception as e:
        jsonify({'error': str(e)}), 400

    errors = {}
    if len(password) < 8:
        errors['password'] = "Your password must be at least 8 characters."

    if len(name) <= 3:
        errors['name'] = "You must specify a name of at least 3 characters."

    if len(errors.keys()) != 0:
        response_object = {
            'error': errors
        }
        return jsonify(response_object), 411

    insertionresult = add_user(name, email, bcrypt.generate_password_hash(
        password=password.encode('utf8')).decode("utf-8"))
    if 'error' in insertionresult:
        errors['email'] = insertionresult["error"]

    make_admin(email)
    userdata = get_user(email)

    if not userdata:
        errors['general'] = "Internal error, please try again later."

    if len(errors.keys()) != 0:
        response_object = {
            'error': errors
        }
        return make_response(jsonify(response_object)), 400
    else:

        userdata = {
            "email": userdata['email'],
            "name": userdata['name'],
            "preferences": userdata.get('preferences'),
            "isAdmin": True
        }

        user = User(userdata)
        jwt = create_access_token(user.to_json())

        try:
            login_user(user.email, jwt)
            response_object = {
                'auth_token': jwt,
                'info': userdata
            }
            return make_response(jsonify(response_object)), 201
        except Exception as e:
            response_object = {
                'error': {'internal': str(e)}
            }
            return make_response(jsonify(response_object)), 500


def check_admin(user):
    updated_user = get_user(user.email)
    return updated_user.get('isAdmin', False)
