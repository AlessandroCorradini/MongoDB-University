#!/usr/bin/env python
"""
Simulates a library app.

Run with --init to initialize the database
Run with --simulate to simulate the database

Usage:
    ./library_db_simulator.py (--init|--simulate) [options]

Options:
    -h --help           Show this text.
    --init              Initialize the database.
    --simulate          Simulate the application.
    -d, --db <db>       Database [default: m312]
    -p, --port <port>   Port [default: 30000]
    --host <host>       Hostname [default: localhost]
    --rs <name>         Replica set name [default: m312RS]
    --publishers <num>  Number of publishers [default: 10]
    --books <num>       Number of books [default: 10000]
    --users <num>       Number of users [default: 1000]
    --authors <num>     Number of authors [default: 1500]
    --city <city>       City where library is located [default: Cityville]
    --state <STATE>     State code where library is [default: ST]
    --debug             Additional information printed while running.
"""

from datetime import datetime, timedelta
from random import choice, randint, shuffle

from docopt import docopt
from faker import Factory
from pymongo import ASCENDING, MongoClient, collection

_SEED = "SEED"
_CHECKOUT_DURATION = timedelta(14)  # 2 weeks
_NON_FICTION_TOPICS = (
    "General works, Computer science and Information",
    "Philosophy and psychology", "Religion", "Social sciences", "Language",
    "Pure Science", "Technology", "Arts & recreation", "Literature",
    "History & geography")
_FICTION_GENRES = (
    "Science fiction", "Satire", "Drama", "Action and Adventure", "Romance",
    "Mystery", "Horror", "Children's", "Poetry", "Comics", "Fantasy")


def create_fake_address(fake, city="Cityville", state="ST", label="home"):
    if city is None:
        city = fake.city()
    if state is None:
        state = fake.state_abbr()
    return {"street": fake.street_address(), "city": city, "state": state,
            "zip": fake.zipcode(), "label": label}


def create_fake_birthday(fake):
    min_age = timedelta(365 * 18)
    max_age = timedelta(365 * 80)  # 18-80 yrs old.
    now = datetime.now()
    return fake.date_time_between(now - max_age, now - min_age)


def create_fake_book_title(fake):
    random_int = randint(0, 2)
    first_name = fake.first_name()
    job = fake.job()
    place = fake.city()
    if random_int == 0:
        title = "{name} the {job}".format(name=first_name, job=job)
    elif random_int == 1:
        title = "The {job} of {place}".format(job=job, place=place)
    elif random_int == 2:
        title = "{name} of {place}".format(name=first_name, place=place)
    return title


def create_fake_book(fake, db, author=None, publisher=None, num_authors=0,
                     all_authors=None, debug=False):
    if publisher is None:
        all_publishers = get_all_publishers(db=db)
        publisher = choice(all_publishers)
    if all_authors is None:
        all_authors = get_all_authors(db=db)
    if author is None:
        if num_authors == 0:
            num_authors = 1
            while (fake.boolean()):  # shouldn't give us an infinite number...
                num_authors += 1
        authors = [choice(all_authors) for i in xrange(num_authors)]
    else:
        authors = [author]
    book = {"title": create_fake_book_title(fake),
            "publisher": publisher["name"],
            "publication_date": fake.date_time_this_century(),
            "authors": authors, "description": fake.paragraph()}
    if fake.boolean():
        book["type"] = "fiction"
        book["subtype"] = choice(_FICTION_GENRES)
    else:
        book["type"] = "non-fiction"
        book["subtype"] = choice(_NON_FICTION_TOPICS)
    return book


def create_phone_number(fake, label="home"):
    phone_number = fake.phone_number()
    if 'x' in phone_number:
        phone_number, extension = phone_number.split('x')
    else:
        extension = None
    if label in ["home", "mobile"]:
        extension = None
    return {"number": phone_number, "extension": extension, "label": label}


def create_fake_user(fake, city="Cityville", state="ST"):
    last_name = fake.last_name()
    first_name = fake.first_name()
    user_name = fake.user_name()
    birth_date = create_fake_birthday(fake)
    user_doc = {"user_name": user_name, "last_name": last_name,
                "first_name": first_name, "birth_date": birth_date,
                "phone_numbers": [create_phone_number(fake, label=label)
                                  for label in ("home", "work", "cell")],
                "addresses": [create_fake_address(fake, city=city,
                                                  state=state)],
                "books_checked_out": []}
    return user_doc


def add_users_to_database(fake, db, user=None, N=1000000, city=None,
                          state=None, debug=False):
    """
    Creates fake users if none are given.

    Inserts them into the database in the 'users' collection.
    """
    if debug:
        print ("Adding {N} users in city, state = ({city}, {state})."
               ).format(N=N, city=city, state=state)
        start_time = datetime.now()

    if user is None:
        users = [create_fake_user(fake=fake, city=city, state=state)
                 for i in xrange(N)]
    else:
        users = [user]
    result = db.users.insert_many(users)
    end_time = datetime.now()

    if debug:
        dt = (end_time - start_time).total_seconds()
        print "    took {dt} seconds.".format(dt=dt)
        dt_per = dt / N
        print "    ... or {dt_per} seconds per unit.".format(dt_per=dt_per)
        print "  ... Done adding users."

    return result


def add_publishers_to_database(fake, db, publisher=None, city=None, state=None,
                               N=1, debug=False):
    """
    Creates N fake publishers if no publisher given.

    Inserts them into the database in the 'publishers' collection.
    """
    if debug:
        print "Adding publishers..."
        start_time = datetime.now()
    if publisher is None:
        publishers = [create_fake_publisher(fake=fake, city=city, state=state)
                      for i in xrange(N)]
    else:
        publishers = [publisher]
    result = db.publishers.insert_many(publishers)
    end_time = datetime.now()
    if debug:
        dt = (end_time - start_time).total_seconds()
        print "    took {dt} seconds.".format(dt=dt)
        dt_per = dt / N
        print "    ... or {dt_per} seconds per unit.".format(dt_per=dt_per)
        print "  ... Done adding publishers."
    return result


def create_fake_publisher(fake, city=None, state=None):
    publisher_document = {"name": fake.company(),
                          "address": create_fake_address(
                              fake, city=city, state=state, label="work"),
                          "phone": create_phone_number(fake, label="work")}
    return publisher_document


def get_all_publishers(db):
    return [publisher
            for publisher in db.publishers.find({}, {"_id": 0, "name": 1})]


def get_all_user_ids(db):
    users = [user["_id"] for user in db.users.find({}, projection=["_id"])]
    return users


def get_all_books(db):
    return [book for book in db.books.find({})]


def get_all_authors(db):
    return [author for author in db.authors.find()]


def create_author(fake):
    return {"last_name": fake.last_name(), "first_name": fake.first_name()}


def add_authors_to_database(fake, db, author=None, N=1, debug=False):
    """
    Creates fake authors and adds them to the database.
    """
    if debug:
        print "Adding authors."
        start_time = datetime.now()
    if author is None:
        authors = [create_author(fake) for i in xrange(N)]
    else:
        authors = [author]
    result = db.authors.insert_many(authors)
    end_time = datetime.now()
    if debug:
        dt = (end_time - start_time).total_seconds()
        print "    took {dt} seconds.".format(dt=dt)
        dt_per = dt / N
        print "    ... or {dt_per} seconds per unit.".format(dt_per=dt_per)
        print "  ... Done adding authors."
    return result


def add_books_to_database(fake, db, book=None, all_publishers=None,
                          all_authors=None, N=100000, debug=False):
    """
    Creates fake books and adds them to the database.
    """
    if debug:
        print "Adding books."
        start_time = datetime.now()
    if all_publishers is None:
        all_publishers = get_all_publishers(db)

    if all_authors is None:
        all_authors = get_all_authors(db)
    if book is None:
        books = [create_fake_book(fake, db, publisher=choice(all_publishers),
                                  all_authors=all_authors)
                 for i in xrange(N)]
    else:
        books = [book]
    result = db.books.insert_many(books)
    if debug:
        end_time = datetime.now()
        dt = (end_time - start_time).total_seconds()
        print "    took {dt} seconds.".format(dt=dt)
        dt_per = dt / N
        print "    ... or {dt_per} seconds per unit.".format(dt_per=dt_per)
        print "  ... Done adding books."
    return result


def create_fake_book_checkout_time(fake, now=None, max_renewals=2,
                                   checkout_duration=_CHECKOUT_DURATION):
    if now is None:
        now = datetime.now()
    num_renewals = 0
    while (fake.boolean() and num_renewals < max_renewals):
        num_renewals += 1
    start_time = now - (checkout_duration * (num_renewals + 1))
    checked_out_time = fake.date_time_between(start_time, now)
    return checked_out_time


def create_check_out_doc(book, checkout_dt, due_date):
    return {"book_id": book["_id"], "title": book["title"],
            "author(s)": book["authors"], "checked_out_on": checkout_dt,
            "due_date": due_date}


def who_checked_out(db, book_id):
    checked_out_by = db.users.find_one({"books_checked_out.book_id": book_id})
    try:
        return checked_out_by["_id"]
    except TypeError:  # no user has checked out the book.
        return None


def check_out_book_initial(db, user_id, book, dt=None, due_date=None,
                           checkout_duration=timedelta(14)):
    if dt is None:
        dt = datetime.now()
    if due_date is None:
        due_date = dt + checkout_duration
    checked_out_by = who_checked_out(db=db, book_id=book["_id"])
    if checked_out_by is None:
        check_out_doc = create_check_out_doc(book, checkout_dt=dt,
                                             due_date=due_date)
        db.users.update_one(
            {"_id": user_id},
            {"$push": {"books_checked_out": check_out_doc}})
    else:
        return None  # can't check out book.


def generate_book_checkouts(fake, db, max_books=6, max_renewals=2,
                            checkout_duration=_CHECKOUT_DURATION, debug=False):
    if debug:
        print "Checking out books."
        start_time = datetime.now()
        print "  ... getting user id's."

    user_ids = get_all_user_ids(db)

    if debug:
        print "  ... Done getting user_id's. Shuffling them."

    shuffle(user_ids)

    if debug:
        print "  ... Done shuffling user_id's. Getting books."

    books = get_all_books(db)

    if debug:
        print "  ... Done getting books. Shuffling them."

    shuffle(books)

    if debug:
        print "  ... Done shuffling books."

    # 10% of users have 1+ books
    num_users_with_books = len(user_ids) / 10
    N = num_users_with_books
    users_with_books = user_ids[:num_users_with_books]
    now = datetime.now()
    if debug:
        print "  ... done finding users and books."
        print "  ... checking out books, and printing one dot per user:",

    for user_id in users_with_books:
        num_books_checked_out = 1
        if fake.boolean():  # half of people have 1 book.
            num_books_checked_out = randint(2, max_books)
        for i in xrange(num_books_checked_out):
            book = books.pop()
            check_out_time = create_fake_book_checkout_time(
                fake, now=now, max_renewals=2,
                checkout_duration=checkout_duration)
            num_renewals = randint(0, max_renewals)
            due_date = check_out_time + (num_renewals * checkout_duration)
            check_out_book_initial(db=db, user_id=user_id, book=book,
                                   dt=check_out_time, due_date=due_date,
                                   checkout_duration=checkout_duration)

        if debug:
            print '.',

    if debug:
        end_time = datetime.now()
        dt = (end_time - start_time).total_seconds()
        print "    took {dt} seconds.".format(dt=dt)
        dt_per = dt / N
        print "    ... or {dt_per} seconds per unit.".format(dt_per=dt_per)
        print " ... Done checking out books."

    return True


def initialize_data(fake, db, city, state, num_publishers=100,
                    num_authors=10000, num_books=15000, num_users=1000000,
                    debug=False, checkout_duration=_CHECKOUT_DURATION):
    client = db.client
    client.drop_database(db.name)
    db.users.create_index([("last_name", ASCENDING),
                           ("first_name", ASCENDING)])
    db.users.create_index(
        [("books_checked_out.book_id", ASCENDING)], unique=True,
        partialFilterExpression={"books_checked_out.book_id": {"$exists": True}})
    db.users.create_index([("books_checked_out.due_date", ASCENDING)])

    db.books.create_index([("authors.last_name", ASCENDING),
                           ("authors.first_name", ASCENDING)])
    db.books.create_index([("title", ASCENDING)])
    db.books.create_index([("type", ASCENDING), ("subtype", ASCENDING),
                           ("author.last_name", ASCENDING),
                           ("author.first_name", ASCENDING)])
    db.authors.create_index([("last_name", ASCENDING),
                             ("first_name", ASCENDING)])
    add_publishers_to_database(fake=fake, db=db, N=num_publishers, debug=debug)
    add_authors_to_database(fake=fake, db=db, N=num_authors, debug=debug)
    add_books_to_database(fake=fake, db=db, N=num_books, debug=debug)
    num_users_out_of_city = num_users / 10
    num_users_out_of_state = num_users / 100
    num_users_in_city = (num_users - num_users_out_of_city -
                         num_users_out_of_state)
    add_users_to_database(fake=fake, db=db, N=num_users_in_city, city=city,
                          state=state, debug=debug)
    add_users_to_database(fake=fake, db=db, N=num_users_out_of_city,
                          state=state, debug=debug)
    add_users_to_database(fake=fake, db=db, N=num_users_out_of_state,
                          debug=debug)
    generate_book_checkouts(fake=fake, db=db, debug=debug)


def get_user_names(db, user_id):
    user_doc = db.users.find_one({"_id": user_id})
    first_name = user_doc["first_name"]
    last_name = user_doc["last_name"]
    return first_name, last_name


def get_book_doc(db, book_id):
    return db.books.find_one({"_id": book_id})


def get_some_books(db, book_ids):
    # ANTI-PATTERN: one query per book
    return {book_id: db.books.find_one({"_id": book_id})
            for book_id in book_ids}
    # Better alternative is commented out: one query for all books
    # return {book["_id"]: book
    #         for book in db.books.find({"_id": {"$in": book_ids}})}


def which_user_has_book(db, book_id):
    return db.users.find_one({"books_checked_out.book_id": book_id})


def tell_user_book_is_checked_out(book_id, user_with_book):
    for book_info in user_with_book:
        if book_info["book_id"] == book_id:
            checked_out_book = book_info["book_id"]
            break
    else:
        raise ValueError(("Passed bad parameters to function " +
                          "'book_is_checked_out'. Expected to find " +
                          "{book_id} in '{user_with_book}', but didn't."
                          ).format(**locals()))
    user_id = user_with_book["_id"]
    title = checked_out_book["title"]
    print ("  ... can't check out '{title}'. It has been checked out by " +
           "user: '{user_id}'.").format(**locals())


def which_books_are_checked_out(db, book_ids):
    # ANTI-PATTERN: query once for each book to see if it's checked out.
    checked_out_books = {}
    for book_id in book_ids:
        user_doc = db.users.find_one({"books_checked_out.book_id": book_id},
                                     {"books_checked_out.$": 1})
        if user_doc is None:
            pass
        else:
            user_id = user_doc["_id"]
            book_subdoc = user_doc["books_checked_out"][0]
            checkout_doc = {"book_id": book_id,
                            "book_title": book_subdoc["title"],
                            "user_id": user_id}
            checked_out_books[book_id] = checkout_doc
    return checked_out_books
    # BETTER VERSION: One query to check all books.
    # return {doc["book_id"]: doc
    #         for doc in db.users.aggregate([
    #      {"$match": {"books_checked_out.book_id": {"$in": book_ids}}},
    #      {"$unwind": "$books_checked_out"},
    #      {"$match": {"books_checked_out.book_id": {"$in": book_ids}}},
    #      {"$project": {"user_id": "$_id",
    #                    "book_id": "$books_checked_out.book_id",
    #                    "book_title": "$books_checked_out.title"}}])}


def check_out(db, user_id, book_ids, checkout_duration=_CHECKOUT_DURATION):
    """
    Does the following:

    * Grabs some data on the books we want from the database
    * Verifies we're not checking out a book that's already checked out.
    * Checks out any books that are left.
    """
    if len(book_ids) > 0:
        can_check_out = True
    book_ids_to_check_out = set(book_ids)
    first_name, last_name = get_user_names(db, user_id)
    books_docs = get_some_books(db, book_ids)

    # list what we want to do
    print ("  User {first_name} {last_name} wants to check out the " +
           "following book(s):").format(**locals())

    for book_id in book_ids:
        book_doc = books_docs[book_id]
        authors = book_doc["authors"]
        book_title = book_doc["title"]
        if len(authors) != 1:
            et_al = " et al."
        else:
            et_al = ""
        author_last = book_doc["authors"][0]["last_name"]
        author_first = book_doc["authors"][0]["first_name"]
        print ("    * '{book_title}' by {author_first} {author_last}{et_al}."
               ).format(**locals())
    print ''

    # Check to see if any books are already checked out.
    #   This happens sometimes; someone returns a book, but it doesn't get
    #   checked back into the system.
    print "  ... checking to see if any of these books are checked out..."
    checked_out_books = which_books_are_checked_out(db, book_ids)
    if checked_out_books == {}:
        print ("  ... and none of them are checked out. " + 
               "Great, let's check them out!")
    else:
        print "\n  ... unfortunately, the following book(s) are checked out:"
        for book_id in checked_out_books:
            title = checked_out_books[book_id]["book_title"]
            print "      * '{title}'".format(title=title)
            book_ids_to_check_out.remove(book_id)
        if len(book_ids_to_check_out) > 0:
            print "  ... but the user can still check out the other book(s)."
        else:
            can_check_out = False
            print "  ... so I'm afraid the user can't check out any books."
    # Actually check out those books not already checked out.
    if can_check_out:
        loan_books_to_user(db=db, user_id=user_id,
                           book_ids=book_ids_to_check_out,
                           books_docs=books_docs,
                           checkout_duration=checkout_duration)


def loan_books_to_user(db, user_id, book_ids, books_docs,
                       checkout_duration=_CHECKOUT_DURATION):
    """
    Checks the books out to the user.

    Doesn't bother to do any verification.
    """
    now = datetime.now()
    due_date = now + checkout_duration
    checkout_docs = []
    for book_id in book_ids:
        book_doc = books_docs[book_id]
        checkout_doc = create_check_out_doc(book_doc, checkout_dt=now,
                                            due_date=due_date)
        checkout_docs.append(checkout_doc)
    # ANTI-PATTERN: one update per book
    for checkout_doc in checkout_docs:
        db.users.update_one({"_id": user_id},
                            {"$push": {"books_checked_out": checkout_doc}})
    user_doc = db.users.find_one({"_id": user_id}, {"books_checked_out": 1})
    # Better method: all updates at once:
    # user_doc = db.users.find_one_and_update(
    #     {"_id": user_id},
    #     {"$pushAll": {"books_checked_out": checkout_docs}},
    #     projection={"books_checked_out": 1},
    #     return_document=collection.ReturnDocument.AFTER)
    print "  The user now has the following books checked out:"
    for book in user_doc["books_checked_out"]:
        title = book["title"]
        due_date = book["due_date"]
        print "    * '{title}' due on {due_date}".format(**locals())


def simulate_check_out_books(db, checkout_duration=_CHECKOUT_DURATION):
    print "Finding users and books."
    print "  ... Don't monitor your database while I do this."
    all_book_ids = [book["_id"] for book in db.books.find({}, {"_id": 1})]
    shuffle(all_book_ids)  # users check out books at random.
    # User gives _id using library card at checkout.
    all_user_ids = [user["_id"] for user in db.users.find({}, {"_id": 1})]
    shuffle(all_user_ids)  # Choose a pseudo-random user each time
    print "  ... Found the set of all users and books from the db."
    print "  OK, you can begin monitoring the database."
    num_books = randint(1, 6)
    end = {1: "one book", 2: "two books", 3: "three books", 4: "four books",
           5: "five books", 6: "six books"}
    while 'q' not in raw_input((
            "Hit enter to have a user check out {end}. " +
            "Hit 'q' to quit.\n").format(end=end[num_books])) == '':
        user_id = all_user_ids.pop()
        book_ids = [all_book_ids.pop() for i in xrange(num_books)]
        check_out(db, user_id, book_ids, checkout_duration=checkout_duration)
        print "  ... Done checking out books for this user."
        num_books = randint(1, 6)


def simulate_application(db, checkout_duration=_CHECKOUT_DURATION):
    print "OK, let's simulate the library's application."
    simulate_check_out_books(db=db, checkout_duration=checkout_duration)


def main():
    opts = docopt(__doc__)
    init = opts['--init']
    simulate = opts['--simulate']
    debug = opts['--debug']
    dbname = opts['--db']
    num_books = int(opts['--books'])
    num_publishers = int(opts['--publishers'])
    num_users = int(opts['--users'])
    num_authors = int(opts['--authors'])
    port = int(opts['--port'])
    city = opts['--city']
    state = opts['--state']
    host = opts['--host']
    replica_set = opts['--rs']
    client = MongoClient(host=host, port=port, replicaSet=replica_set)
    db = client[dbname]
    fake = Factory().create()
    checkout_duration = _CHECKOUT_DURATION
    if init:
        initialize_data(fake=fake, db=db, city=city, state=state,
                        num_publishers=num_publishers, num_authors=num_authors,
                        num_books=num_books, num_users=num_users, debug=debug)
    if simulate:
        simulate_application(db=db, checkout_duration=checkout_duration)


if __name__ == '__main__':
    main()
