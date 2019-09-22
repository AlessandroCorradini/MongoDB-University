#!/usr/bin/env python
"""
Spawns parallel processes that all try to update the same document.

Usage:
    ./write_to_the_same_document.py [options]

Options:
    -h --help           Show this text.
    --host <host>       Host [default: localhost]
    --port <port>       Port [default: 27017]
    -n <processes>      Number of processes to spawn [default: 20]
    -d <database>       Name of the database [default: m312]
    -c <collection>     Name of the collection to use [default: collisions]
    -u <num>            Updates per process [default: 10000]
    -i <_id>            _id to use in the document
    --size <size>       Document padding size, in bytes [default: 10000]
    --docPerProcess     If True, there will be no collisions.
    --replicaSet <set>  Replica set name [default: m312RS]
"""

from multiprocessing import Process

from bson import objectid
from docopt import docopt
from pymongo import MongoClient


def connect_and_update(_id, padding, host, port, dbname, collname,
                       updates_per_process, process_number, replica_set):
    """
    Updates document with _id <updates_per_process> times.
    """
    client = MongoClient(host=[get_hostport_string(host=host, port=port)],
                         replicaset=replica_set)
    db = client[dbname]
    collection = db[collname]
    try:  # Unless using multiple docs, most of these will fail
        collection.insert_one({"_id": _id, "padding": padding})
    except:
        pass

    for j in xrange(updates_per_process):
        update_document(_id, collection, padding, process_number)
    
    client.close()


def update_document(_id, collection, padding, process_number):
    """
    Increments a field on document with `_id`, once.
    """
    collection.update_one(
        {"_id": _id},
        {'$inc': {'process_{p}.updates'.format(p=process_number): 1,
                  'total_updates': 1}})


def get_hostport_string(host='localhost', port=27017):
    return '{host}:{port}'.format(host=host, port=port)


def main():
    # Assign variables to the options & set type appropriately.
    opts = docopt(__doc__)  # I <3 docopt
    host = opts['--host']
    dbname = opts['-d']
    collname = opts['-c']
    padding_size = int(opts['--size'])
    port = int(opts['--port'])
    padding = '0'.join(['' for i in xrange(padding_size)])
    num_processes = int(opts['-n'])
    updates_per_process = int(opts['-u'])
    _id = opts['-i']
    document_per_process = opts['--docPerProcess']
    replica_set = opts['--replicaSet']
    if _id is None:
        _id = objectid.ObjectId()

    # Initialize the collection
    client = MongoClient(host=[get_hostport_string(host=host, port=port)],
                         replicaset=replica_set)
    db = client[dbname]
    collection = db[collname]
    collection.drop()  # Drop the collection to start fresh
    client.close()

    # Spawn processes, each of which updates documents.
    for i in xrange(num_processes):
        if document_per_process:
            _id = objectid.ObjectId()
        p = Process(target=connect_and_update,
                    args=(_id, padding, host, port, dbname, collname,
                          updates_per_process, i, replica_set))
        p.start()


if __name__ == '__main__':
    main()
