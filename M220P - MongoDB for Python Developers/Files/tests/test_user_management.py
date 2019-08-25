import pytest
from mflix.db import delete_user, get_user_session, get_user, add_user, \
    login_user, logout_user

test_user = {
    'name': 'Magical Mr. Mistoffelees',
    'email': 'magicz@cats.com',
    'password': 'somehashedpw',
    'jwt': 'someneatjwt'
}


@pytest.mark.user_management
def test_registration(client):
    delete_user('magicz@cats.com')
    # the password will be hashed at the api layer
    # NEVER
    # NEVER
    # NEVER store passwords in plaintext

    result = add_user(test_user.get('name'), test_user.get(
        'email'), test_user.get('password'))

    assert result == {'success': True}

    found_user = get_user(test_user.get('email'))
    assert found_user.get('name') == test_user.get('name')
    assert found_user.get('email') == test_user.get('email')
    assert found_user.get('password') == test_user.get('password')


@pytest.mark.user_management
def test_no_duplicate_registrations(client):
    result = add_user(test_user.get('name'), test_user.get(
        'email'), test_user.get('password'))

    assert result == {'error': "A user with the given email already exists."}


@pytest.mark.user_management
def test_login(client):
    result = login_user(test_user.get('email'), test_user.get('jwt'))
    assert result == {'success': True}
    session_result = get_user_session(test_user.get('email'))
    assert session_result.get('user_id') == test_user.get('email')
    assert session_result.get('jwt') == test_user.get('jwt')


@pytest.mark.user_management
def test_logout(client):
    result = logout_user(test_user.get('email'))
    assert result == {'success': True}
    session_result = get_user_session(test_user.get('email'))
    assert session_result is None
