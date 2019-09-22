#!/usr/bin/env python
"""
Times the total time to get a { w : "majority" } write acknowledged.

Usage:
    ./time_w_majority_writes.py [options]

Options:
    -h --help                               Show this text.
    --host <host>                           Hostname where the mongod is running [default: localhost]
    -p, --port <port>                       Port on which a mongod is listening [default: 30000]
    -d, --db <db>                           Database name [default: m312]
    -c, --collection <coll>                 Collection to use [default: testingW]
    --replicaSetName <replicaSetName>       Name of the replica set [default: m312RS]
    -n <timesToTry>                         Number of times to retry the write [default: 10]
    -t, --serverSelectionTimeoutMS <ms>     Milliseconds before the timeout occurs.
    -w, --writeConcernW <w>                 Write concern 'w' parameter [default: majority]
    --wTimeOut <ms>                         Timeout to get acknowledgment from a write
    --username <username>                   MongoDB username, if auth is enabled.
    --password <password>                   MongoDB password, if auth is enabled.
    --authDB <dbname>                       Database against which to authenticate [default: admin]
    --ssl                                   Whether or not to use ssl [default: False]
"""

from __future__ import division
from docopt import docopt
from datetime import datetime
from pymongo import errors, MongoClient
from math import sqrt


def mean(numbers):
    """
    Finds the mean of a list of numbers.
    """
    return sum(numbers) / len(numbers)


def std(numbers):
    """
    Finds the standard deviation of a list of numbers.
    """
    avg_num = mean(numbers)
    mean_square = mean([((num - avg_num) * (num - avg_num)) for num in numbers])
    return sqrt(mean_square)


def get_how_much_time_has_passed(start_time):
    """
    Calculates time from start_time to now, and prints a message.

    Inputs
    ------
    start_time (datetime)
    """
    end_time = datetime.now()
    return (end_time - start_time).total_seconds()


def get_client(host='localhost', port=27017, serverSelectionTimeoutMS=None,
               socketTimeoutMS=None, replicaSet=None, waitQueueTimeoutMS=None,
               wtimeout=None, w="majority", ssl=None):
    kwargs = {k: v for k, v in locals().items() if v is not None}
    if host is None:
        pass
    elif ',' in host:
        host = host.split(',')  # make a comma-separated list.
    return MongoClient(**kwargs)


def authenticate(client, username, password, auth_db):
    """
    Authenticates the user for the client connection.
    """
    db = client[auth_db]
    try:
        db.authenticate(username, password)
    except errors.ConfigurationError as e:
        print ("Got a ConfigurationError when trying to authenticate: {e}"
               ).format(e=e)
        exit()
    except errors.ServerSelectionTimeoutError as e:
        print ("Got a ServerSelectionTimeoutError when trying to " +
               "authenticate: {e}".format(e=e))
        print ("Are you sure your username & password are correct?")
        print ("  ... username = {username}".format(username=username))
        print ("  ... password = {password}".format(password=password))
        exit()


def perform_writes(collection, sample_size=10):
    write_times = []
    for i in xrange(sample_size):
        print "  ... sending a write."
        start_time = datetime.now()
        try:
            insert_response = collection.insert_one({})
            time_passed = get_how_much_time_has_passed(start_time=start_time)
            if insert_response.acknowledged is False:
                print ("Insert went unacknowledged.\n" +
                       "  _id={id}".format(_id=insert_response.inserted_id))
                print ("  Successfully inserted {n} documents.".format(n=(i-1)))
                exit()
            else:
                write_times.append(time_passed)
        except errors.WriteConcernError as e:
            print "  ... Got a WriteConcernError: {e}".format(e=e)
            exit()

    return write_times


def print_write_summary(write_times):
    print "The writes took the following amounts of time each:"
    for write_time in write_times:
        print "  {write_time}".format(write_time=write_time)
    mean_write_time = mean(write_times)
    print "Summary:"
    try:
        print "  The average time was {mean}".format(mean=mean_write_time)
        print "  The shortest time was {min_t}".format(min_t=min(write_times))
        print "  The longest time was {max_t}".format(max_t=max(write_times))
        print ("  The standard deviation was " +
               "{stdev}.").format(stdev=std(write_times))
    except ValueError as e:
        print "  ... Can't print the summary."
        print "  ... Got a ValueError: {e}".format(e=e)
        print "  ... Perhaps none of your writes succeeded?"


def show_replica_set_info(client):
    nodes = client.nodes
    primary = client.primary
    write_concern = str(client.write_concern)
    print "Replica set info:"
    print "  {n} members".format(n=len(nodes))
    print ("  primary: '{primary_host}:{primary_port}'"
           ).format(primary_host=primary[0], primary_port=primary[1])
    print "  {w}".format(w=write_concern)


def get_int_or_none(value):
    """
    If value is a string, tries to turn it into an int.
    """
    if type(value) is str:
        return int(value)
    else:
        return value


def main():
    opts = docopt(__doc__)
    port = int(opts['--port'])
    host = opts['--host']
    replica_set_name = opts['--replicaSetName']
    serv_sel_timeout_ms = get_int_or_none(opts['--serverSelectionTimeoutMS'])
    w = opts['--writeConcernW']
    if w != 'majority':  # then write concern should be a number
        w = int(w)
    w_timeout_ms = opts['--wTimeOut']
    sample_size = int(opts['-n'])
    dbname = opts['--db']
    collname = opts['--collection']
    username = opts['--username']
    password = opts['--password']
    auth_db = opts['--authDB']
    ssl = opts['--ssl']
    client = get_client(host=host, port=port, w=w, ssl=ssl,
                        serverSelectionTimeoutMS=serv_sel_timeout_ms,
                        replicaSet=replica_set_name, wtimeout=w_timeout_ms)
    db = client[dbname]
    collection = db[collname]
    if ssl:
        authenticate(client=client, username=username, password=password,
                     auth_db=auth_db)
    else:
        if (username is not None) or (password is not None):
            print "SSL flag not set. Not going to authenticate."
    write_times = perform_writes(collection, sample_size=sample_size)
    print_write_summary(write_times)
    show_replica_set_info(client)


if __name__ == '__main__':
    main()
