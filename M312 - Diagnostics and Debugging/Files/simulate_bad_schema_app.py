#!/usr/bin/env python
"""
Runs a simulated application.

Usage:
    ./simulate_bad_schema_app.py [options]

Options:
    -h --help           Show this text.
    -p, --port <port>   Port where a server isn't listening [default: 30000]
    --host <host>       Host where a port is open. If multiple hosts are given,
                            separate them with commas, and it will be parsed as
                            a list. [default: localhost]
    --db <dbname>       Database to use [default: m312]
    --rs <rsname>       Replica set name [default: m312RS]
    --user <username>   Log in as username.
    --mode (login)      Run the login functionality [default: login]
"""

from datetime import datetime
from random import choice

from docopt import docopt
from pymongo import DESCENDING, MongoClient


def get_client(host='localhost', port=27017, serverSelectionTimeoutMS=None,
               socketTimeoutMS=None, replicaSet=None, waitQueueTimeoutMS=None):
    kwargs = {k: v for k, v in locals().items() if v is not None}
    if host is None:
        pass
    elif ',' in host:
        host = host.split(',')  # make a comma-separated list.
    return MongoClient(**kwargs)


def record_start_of_session(db, user_id):
    """Records the fact that the user is now logging in."""
    db.logins.insert_one({"login_date": datetime.now(), "ip_address": "127.0.0.1", "user_id": user_id, "user_agent": "w3m/0.5.3"})


def find_all_usernames(db):
    """Queries the database for all usernames. Returns a list."""
    return [user["user_name"] for user in db.users.find({})]


def find_time_since(previous_time):
    """Just finds the time between now and previous_time."""
    return datetime.now() - previous_time


def find_cart(db, user_id):
    """
    Finds a user's cart.

    The cart is in the `sales` database, where "completed" is False.
    """
    curs = db.sales.find({"buyer_id": user_id, "completed": False})
    return curs


def get_addresses(db, user_id):
    """
    Queries the database for the addresses associated with the user.

    These include home, shipping, and billing addresses.

    Starts by querying user_addresses for addresses associated with the user,
        and finds what their their _id is in the addresses collection. Follows
        this with a query of the addresses collection.

    This is an anti-pattern in MongoDB, but one which might be used in a
        relational database, if the engineers wanted to ensure that no
        addresses were duplicated, even if they were shared by two users, as
        one might find with people who share a home.
    """
    curs = db.user_addresses.find({"user_id": user_id},
                                  {"_id": 0, "address_id": 1})
    addresses = []
    for doc in curs:
        address_id = doc["address_id"]
        address = db.addresses.find_one(
            {"_id": address_id}, {"_id": 0})["address"]
        if "type" in doc:
            address_type = doc["type"]
            address["address_type"] = address_type
        addresses.append(address)
    return addresses


def get_phone_numbers(db, user_id):
    """
    Queries the database for the user's phone numbers.

    Starts by querying the "user_phone_numbers" collection for the phone
        numbers associated with the user_id, then queries the "phone numbers"
        collection for the appropriate phone numbers.

    This is an anti-pattern in MongoDB, but one which might be used in a
        relational database, if the engineers wanted to ensure that no phone
        numbers were duplicated, even if they were shared by two users, as one
        might find with people who share a home and share their home phone
        number.
    """
    curs = db.user_phone_numbers.find({"user_id": user_id},
                                      {"_id": 0, "label": 1,
                                       "phone_number_id": 1})
    phone_numbers = []
    for doc in curs:
        phone_number_id = doc["phone_number_id"]
        label = doc["label"]
        phone_number_doc = db.phone_numbers.find_one({"_id": phone_number_id},
                                                     {"_id": 0})
        if label:
            phone_number_doc["label"] = label
        phone_numbers.append(phone_number_doc)
    return phone_numbers


def get_user_information(db, user_id):
    """
    Finds the addresses and phone numbers associated with the user.
    """
    addresses = get_addresses(db, user_id)
    phone_numbers = get_phone_numbers(db, user_id)
    return {"addresses": addresses, "phone_numbers": phone_numbers}


def login_script(user, db):
    """
    Displays the text the user might see on a landing page.

    If no user is specified, chooses one at random.
    """
    if user is None:
        print "I see you haven't chosen a user. Selecting one at random."
        print "This will involve querying the users collection."
        all_users = find_all_usernames(db)
        loop_it = True
        if len(all_users) == 0:
            raise ValueError("In querying the database, I found no " +
                             "documents. Are you sure you loaded your " +
                             "data? Exiting.")
    else:
        loop_it = False
    res = raw_input("OK, ready to begin login. Hit 'enter' to continue, " +
                    "or 'q' to quit.\n")
    if res != 'q':
        query_user = True
    else:
        query_user = False
    while query_user:
        user = choice(all_users)
        print "Logging is as user '{user}'.".format(user=user)
        user_doc = db.users.find_one({"user_name": user})
        if user_doc is None:
            raise ValueError("You have tried to log in as a user that " +
                             "doesn't exist: '{user}'.\n".format(user=user) +
                             "Exiting.")

        user_id = user_doc["_id"]

        print "Name: {full_name}\nUsername: {user_name}".format(**user_doc)
        last_login = find_last_login(db, user_id)
        print ("Your last login was on {last_login}."
               ).format(last_login=last_login)
        time_since_last_login = find_time_since(last_login)
        print ("It has been {dt} since your last login. Welcome back!"
               ).format(dt=time_since_last_login)
        # This has to happen after checking last login:
        record_start_of_session(db, user_id)
        cart = [doc for doc in find_cart(db, user_id)]
        if len(cart) == 0:
            print "Looks like you don't have anything in your cart."
        else:
            print "Looks like you have a cart ready for checkout."
            print "Here are the contents:"
            total = 0
            for doc in cart:
                product_id = doc["product_id"]
                product = db.products.find_one({"_id": product_id})
                quantity = product["quantity"]
                price = product["price"]
                title = product["title"]
                subtotal = price * quantity
                print "  Name: {title}".format(title=title)
                print "    Quantity: {quantity}".format(quantity=quantity)
                print ("    Price (each): {price} {unit}"
                       ).format(price=price, unit=product["currency"])
                print "    Subtotal: {subtotal}".format(subtotal=subtotal)
                total += subtotal
            print "Total: {total} USD ($)".format(total=total)
        print "\nPlease review your contact information:"
        user_info = get_user_information(db=db, user_id=user_id)
        print "  Phone number(s):"
        for phone_number in user_info["phone_numbers"]:
            print ("    number: {number}"
                   ).format(number=phone_number["phone_number"])
            print ("    extension: {extension}"
                   ).format(extension=phone_number["extension"])
            if "label" in phone_number:
                print ("    type: {label}"
                       ).format(label=phone_number["label"])

        print "  Address(es):"
        for address in user_info["addresses"]:
            print "    Street: {street}".format(street=address["street"])
            print "    City: {city}".format(city=address["city"])
            print "    State: {state}".format(state=address["state"])
            print "    Zip: {zip_code}".format(zip_code=address["zip_code"])
        if loop_it:
            res = raw_input("OK, ready to begin another login. Hit 'enter' " +
                            "to continue, or 'q' to quit.\n")
            if res != 'q':
                query_user = True
            else:
                query_user = False
    print "Good-bye!"


def find_last_login(db, user_id):
    """
    Finds user's last login date.
    """
    curs = db.logins.find({"user_id": user_id}, {"_id": 0, "login_date": 1})
    curs.sort([("login_date", DESCENDING)]).limit(1)
    return curs.next()["login_date"]


def main():
    opts = docopt(__doc__)
    host = opts['--host']
    port = int(opts['--port'])
    dbname = opts['--db']
    user = opts['--user']
    mode = opts['--mode']

    client = get_client(host=host, port=port)
    db = client[dbname]

    if mode == 'login':
        login_script(user, db)


if __name__ == '__main__':
    main()
