#!/usr/bin/env python
"""
Generates fake data for the schema.

Usage:
    ./simulated_bad_schema.py [options]

Options:
    -h --help               Show this text.
    -p, --port <port>       Port where a server isn't listening
                                [default: 30000]
    --host <host>           Host where a port is open. If multiple hosts are
                                given, separate them with commas, and it will
                                be parsed as a list. [default: localhost]
    --db <dbname>           Database to use [default: m312]
    --rs <rsname>           Replica set name [default: m312RS]
    --users <num>           Number of users in the data set [default: 1000]
    --sellers <num>         Number of sellers in the data set [default: 10]
    --products <num>        Number of products in the data set [default: 50]
    --reviews <num>         Number of reviews of products in the data set
                                [default: 1000]
    --loginsPerUser <num>   Number of logins per user, on average [default: 20]
"""

from __future__ import division

from copy import deepcopy
from datetime import datetime, timedelta
from random import choice, randint, random, shuffle

from bson import objectid
from docopt import docopt
from faker import Factory
from pymongo import ASCENDING, DESCENDING, MongoClient, errors

TEN_MINUTES = timedelta(0, (60*10))
PRODUCT_NAMES = ["Alarm clock", "Armoire", "Backpack", "Bedding", "Bedspread",
                 "Binder", "Blanket", "Blinds", "Bookcase", "Book", "Broom",
                 "Brush", "Bucket", "Calendar", "Candle", "Carpet", "Chair",
                 "Chair", "China", "Clock", "Coffee table", "Comb",
                 "Comforter", "Computer", "Food Container", "Couch",
                 "Credenza", "Cup", "Curtains", "Cushion", "Desk",
                 "Dish towel", "Dishwasher", "Door stop", "Drapes", "Dresser",
                 "Drill", "Dryer", "Dust pan", "Duvet", "End tables",
                 "Extension cord", "Fan", "Figurine", "File cabinet",
                 "Fire extinguisher", "Flashlight", "Flatware", "Flowers",
                 "Forks", "Furnace", "Glasses", "Hammer", "Heater",
                 "Houseplant", "Mobile Phone", "Portable Media Player", "Iron",
                 "Ironing board", "Jewelry", "Knives", "Lamp", "Light bulbs",
                 "Light switch", "Linens", "Magnet", "Marker", "Medicine",
                 "Microwave", "Mop", "Mug", "Musical instrument", "Napkins",
                 "Note paper", "Oven", "Painting", "Pan", "Pants", "Paper",
                 "Pen", "Pencil", "Phone", "Photograph", "Piano", "Pillow",
                 "Pitcher", "Plants", "Plastic plates", "Plates", "Pliers",
                 "Pot", "Radiator", "Radio", "Rags", "Refrigerator",
                 "Rubber duck", "Rug", "Saucer", "Saw", "Scissors",
                 "Screw driver", "Settee", "Sunglasses", "Sheets", "Shelf",
                 "Shirt", "Shoes", "Smoke detector", "Sneakers", "3 Socks",
                 "Sofa", "Speakers", "Spoons", "Suitcase", "Supplies",
                 "Sweeper", "Tablecloth", "Table", "Telephone",
                 "Digital Timer", "Box of Tissues", "Toaster", "Toilet paper",
                 "Toothbrush", "Toothpaste", "4 Towels", "TV", "Vacuum",
                 "Vase", "Washer", "Washing machine"]


def random_birthday(fake):
    min_age = timedelta(365 * 18)
    max_age = timedelta(365 * 80)  # 18-80 yrs old.
    now = datetime.now()
    return fake.date_time_between(now - max_age, now - min_age)


def generate_address(fake, db, user_id, address_type=None):
    address = {"street": fake.street_address(), "city": fake.city(),
               "state": fake.state(), "zip_code": fake.zipcode_plus4()}
    try:
        address_id = db.addresses.insert_one(
            {"address": address}).inserted_id
    except errors.DuplicateKeyError:
        address_id = db.addresses.find_one({"address": address})['_id']
    db.user_addresses.insert_one(
        {"user_id": user_id, "address_id": address_id, "type": address_type})
    return address_id


def get_client(host='localhost', port=27017, serverSelectionTimeoutMS=None,
               socketTimeoutMS=None, replicaSet=None, waitQueueTimeoutMS=None):
    kwargs = {k: v for k, v in locals().items() if v is not None}
    if host is None:
        pass
    elif ',' in host:
        host = host.split(',')  # make a comma-separated list.
    return MongoClient(**kwargs)


def generate_phone_number(fake, db, user_id, label="home"):
        phone_number = fake.phone_number()
        if 'x' in phone_number:
            phone_number, extension = phone_number.split('x')
        else:
            extension = None
        if label in ["home", "mobile"]:
            extension = None
        return input_phone_number(
                   fake=fake, db=db, user_id=user_id,
                   phone_number=phone_number, label=label, extension=extension)


def input_phone_number(fake, db, user_id, phone_number, label="home",
                       extension=None):
    try:
        phone_number_id = db.phone_numbers.insert_one(
            {"phone_number": phone_number, "extension": extension}).inserted_id
    except errors.DuplicateKeyError:
        # phone number is already present
        phone_number_id = db.phone_numbers.find_one(
            {"phone_number": phone_number, "extension": extension},
            {"_id": 1})["_id"]
    db.user_phone_numbers.insert_one(
        {"user_id": user_id, "phone_number_id": phone_number_id,
         "label": label})
    return phone_number_id


def generate_user(fake, db):
    user_id = objectid.ObjectId()  # we need it now.
    full_name = fake.name()
    user_name = fake.user_name()
    birth_date = random_birthday(fake)
    generate_address(fake, db, user_id)
    password = fake.password()
    user_doc = {"_id": user_id, "user_name": user_name, "full_name": full_name,
                "birth_date": birth_date, "password": password}

    db.users.insert_one(user_doc)

    phone_number_labels = ("home", "cell", "work")
    for phone_label in phone_number_labels:
        generate_phone_number(fake=fake, db=db, user_id=user_id,
                              label=phone_label)

    return user_id


def generate_receipt(fake, db, buyer_id, completed=True,
                     date=None, num_items=None, receipt_id=None):
    if num_items is None:
        num_items = randint(1, 10)
    if date is None:
        date = fake.date_time_this_decade()
    if receipt_id is None and completed is True:
        receipt_id = objectid.ObjectId()
    all_sellers = find_all_sellers(db=db)
    all_products = find_all_products(db=db)
    for i in xrange(num_items):
        product_id = choice(all_products)
        seller_id = choice(all_sellers)
        generate_sale(fake=fake, db=db, buyer_id=buyer_id, completed=completed,
                      date=date, product_id=product_id, seller_id=seller_id,
                      receipt_id=receipt_id)
    return receipt_id


def generate_sale(fake, db, buyer_id, receipt_id, seller_id=None,
                  product_id=None, quantity=None, completed=False, date=None):
    if seller_id is None:
        all_sellers = find_all_sellers(db=db)
        seller_id = choice(all_sellers)
    if product_id is None:
        all_products = find_all_products(db=db)
        product_id = choice(all_products)
    if quantity is None:
        quantity = randint(1, 20)

    return db.sales.insert_one(
        {"buyer_id": buyer_id, "seller_id": seller_id,
         "product_id": product_id, "quantity": quantity, "date": date,
         "completed": completed, "receipt_id": receipt_id}).inserted_id


def generate_product(fake, db, seller_id, price=None, currency=None):
    if price is None:
        price = randint(1, 10000) / 100
    if currency is None:
        currency = "USD ($)"
    return db.products.insert_one(
        {"description": fake.paragraph(), "price": price, "currency": currency,
         "quantity": randint(0, 20), "locale": fake.locale(),
         "seller_id": seller_id, "title": choice(PRODUCT_NAMES)}).inserted_id


def generate_review(fake, db, seller_id, reviewer_id, product_id, review_date):
    return db.reviews.insert_one(
        {"seller_id": seller_id, "rating": randint(1, 5),
         "reviewer_id": reviewer_id, "text": fake.paragraph(),
         "title": fake.sentence(), "date": review_date,
         "product_id": product_id}).inserted_id


def generate_seller(fake, db, user_id=None):
    if user_id is None:
        user_id = generate_user(fake, db)
    seller_id = db.sellers.insert_one(
        {"user_id": user_id, "total_sales": randint(0, 1000000),
         "currency": fake.currency_code(), "user_id": user_id}).inserted_id
    return seller_id


def generate_payment_method(fake, db, user_id, address_id=None):
    if address_id is None:
        address_id = generate_address(fake, db)
    db.user_addresses.insert_one({"user_id": user_id,
                                  "address_id": address_id})


def generate_user_login(fake, db, user_id, ip=None, ua=None, login_date=None):
    if ip is None:
        ip = fake.ipv4()
    if ua is None:
        ua = fake.user_agent()
    if login_date is None:
        login_date = fake.date_time_this_decade()
    return db.logins.insert_one(
        {"user_id": user_id, "login_date": login_date, "ip_address": ip,
         "user_agent": ua}).inserted_id


def generate_users(fake, db, num_users, logins_per_user):
    for i in xrange(num_users):
        user_id = generate_user(fake, db)
        num_logins = randint(1, (2*logins_per_user))
        three_quarters_of_logins = int(num_logins*0.75) - 1
        common_ip = fake.ipv4()
        common_ua = fake.user_agent()
        common_ip_indices = range(num_logins)
        common_ua_indices = range(num_logins)
        shuffle(common_ua_indices)
        shuffle(common_ip_indices)
        common_ua_indices = set(common_ua_indices[:three_quarters_of_logins])
        common_ip_indices = set(common_ip_indices[:three_quarters_of_logins])
        for j in xrange(num_logins):
            if j in common_ua_indices:
                ua = common_ua
            else:
                ua = None
            if j in common_ip_indices:
                ip = common_ip
            else:
                ip = None
            generate_user_login(fake, db, user_id, ip=ip, ua=ua)


def generate_sellers(fake, db, num_sellers):
    all_users = find_all_users(db=db)
    some_users = set(deepcopy(all_users))
    for i in xrange(num_sellers):
        user_id = some_users.pop()
        generate_seller(fake, db, user_id)


def find_all_sellers(db):
    return [seller["_id"] for seller in db.sellers.find({})]


def find_user_login_map(db):
    user_logins = {}
    for user_doc in db.users.find():
        user_id = user_doc["_id"]
        user_logins[user_id] = []
        for login in db.logins.find({"user_id": user_id}):
            user_logins[user_id].append(login["login_date"])
    return user_logins


def generate_reviews(fake, db, num_reviews):
    current_reviews = 0
    current_retries = 0
    db.reviews.create_index([
        ("reviewer_id", ASCENDING), ("seller_id", ASCENDING),
        ("product_id", ASCENDING)], unique=True)

    user_logins = find_user_login_map(db=db)
    all_users = find_all_users(db=db)
    all_sellers = find_all_sellers(db=db)
    all_products = find_all_products(db=db)
    while current_reviews < num_reviews:
        try:
            reviewer_id = choice(all_users)
            seller_id = choice(all_sellers)
            product_id = choice(all_products)
            review_date = choice(user_logins[reviewer_id])
            generate_review(fake=fake, db=db, seller_id=seller_id,
                            reviewer_id=reviewer_id, product_id=product_id,
                            review_date=review_date)
            current_reviews += 1
        except errors.DuplicateKeyError:
            current_retries += 1
            if current_retries > 10 * (current_reviews + 10):
                raise Exception("Too many attempts made to create reviews. " +
                                "Looks like I might be stuck in an " +
                                "infinite loop. Exiting.")


def generate_products(fake, db, num_products):
    all_sellers = find_all_sellers(db=db)
    for i in xrange(num_products):
        seller_id = choice(all_sellers)
        generate_product(fake=fake, db=db, seller_id=seller_id)


def find_all_users(db):
    return [user["_id"] for user in db.users.find({}, {"_id": 1})]


def find_all_products(db):
    return [product["_id"] for product in db.products.find()]


def generate_purchase_history(fake, db):
    user_logins = find_user_login_map(db=db)
    all_users = find_all_users(db=db)
    for user_id in all_users:
        num_logins = db.logins.count({"user_id": user_id})
        num_purchases = int(random() * num_logins / 2)
        for i in xrange(num_purchases):
            purchase_date = choice(user_logins[user_id]) + TEN_MINUTES
            generate_receipt(fake=fake, db=db, buyer_id=user_id,
                             date=purchase_date)
        curs = db.logins.find({"user_id": user_id})
        curs.sort([("login_date", DESCENDING)]).limit(1)
        last_login_date = curs.next()["login_date"]
        # Create a cart the user left from before.
        generate_receipt(fake=fake, db=db, buyer_id=user_id,
                         completed=False, num_items=randint(2, 5),
                         date=last_login_date + TEN_MINUTES)


def generate_data(fake, db, num_users, num_sellers, num_products, num_reviews,
                  logins_per_user):
    """
    Initializes everything and builds fake data.

    - Drops the database.
    - Creates indices that will be used in creating data.
    - Creates data
    - Creates any indices expected to be used going forward.
    """
    db.logins.create_index([("user_id", ASCENDING),
                            ("login_date", ASCENDING)])
    db.sellers.create_index([("user_id", ASCENDING)], unique=True)

    generate_users(fake=fake, db=db, num_users=num_users,
                   logins_per_user=logins_per_user)
    generate_sellers(fake=fake, db=db, num_sellers=num_sellers)
    generate_products(fake=fake, db=db, num_products=num_products)
    generate_reviews(fake=fake, db=db, num_reviews=num_reviews)
    generate_purchase_history(fake=fake, db=db)

    db.users.create_index([("user_name", ASCENDING)])
    db.user_addresses.create_index([("user_id", ASCENDING),
                                    ("address_id", ASCENDING),
                                    ("type", ASCENDING)], unique=True)
    db.addresses.create_index([("address", ASCENDING)], unique=True)
    db.phone_numbers.create_index([("phone_number", ASCENDING),
                                   ("label", ASCENDING)])
    db.sellers.create_index([("total_sales", ASCENDING),
                             ("currency", ASCENDING)])
    db.reviews.create_index([("seller_id", ASCENDING), ("date", ASCENDING)])
    db.reviews.create_index([("reviewer_id", ASCENDING), ("date", ASCENDING)])
    db.reviews.create_index([("rating", ASCENDING), ("date", ASCENDING)])
    db.reviews.create_index([("date", ASCENDING)])
    db.addresses.create_index([("address.state", ASCENDING),
                               ("address.city", ASCENDING)])
    db.sales.create_index([("buyer_id", ASCENDING), ("completed", ASCENDING)])
    db.user_phone_numbers.create_index([("user_id", ASCENDING),
                                        ("phone_number_id", ASCENDING),
                                        ("label", ASCENDING)])


def main():
    opts = docopt(__doc__)
    host = opts['--host']
    port = int(opts['--port'])
    dbname = opts['--db']
    num_users = int(opts['--users'])
    num_sellers = int(opts['--sellers'])
    num_products = int(opts['--products'])
    num_reviews = int(opts['--reviews'])
    logins_per_user = int(opts['--loginsPerUser'])
    client = get_client(host=host, port=port)
    client.drop_database(dbname)
    db = client[dbname]
    fake = Factory.create()
    generate_data(fake=fake, db=db, num_users=num_users,
                  num_sellers=num_sellers, num_products=num_products,
                  num_reviews=num_reviews, logins_per_user=logins_per_user)


if __name__ == '__main__':
    main()
