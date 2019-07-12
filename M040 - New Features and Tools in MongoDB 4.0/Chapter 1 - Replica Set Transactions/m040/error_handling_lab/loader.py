#!/bin/env python3
"""
M040 - Error Handling Lab Loader
--------------------------------
This is a multiprocessing script that processes the dataset file in
batches, where each spawned process will handle a portion of the file.

Usage:
    loader.py [--uri=<uri>] [--file=<file>]

Options:
    -h --help           Show this help text.
    --uri=<uri>         MongoDB connection uri [default: mongodb://m040:27017/m040?replicaSet=M040]
    --file=<file>       Dataset file location [default: ./data.json]
"""
import pymongo
import json
from docopt import docopt
from itertools import islice
from multiprocessing import Process, Queue


def drop_dataset(uri):
    """
    Drop this lab's collections
    """
    mc = pymongo.MongoClient(uri)
    db = mc.m040
    db.drop_collection('cities')
    db.drop_collection('city_stats')


def touch_collections(uri):
    """
    Creates all necessary dataset collections
    """
    mc = pymongo.MongoClient(uri)
    db = mc.m040
    db.city_stats.insert_one({"_id": "loader"})
    db.create_collection('cities')


def handle_commit(s):
    """
    Handles the commit operation.
    """
    # LAB - needs error handling
    try:
        s.commit_transaction()
    except Exception as exc:
        # do something here
        raise


def write_batch(batch, mc, s):
    """
    Executes the batch write operation
    """
    try:
        s.start_transaction()
        result = mc.m040.cities.insert_many(batch, session=s)
        batch_total_population = sum(d['population'] for d in batch)
        mc.m040.city_stats.update_one({'_id': 'loader'},
            {"$inc": {"population_total": batch_total_population}},
            session=s
        )
        handle_commit(s)
    except (pymongo.errors.DuplicateKeyError) as dupex:
        print("Duplicate Key Found: {}".format(dupex))
        s.abort_transaction()
        return(0,0)
    return (batch_total_population,len(result.inserted_ids))


def load_data(q, batch, uri):
    """
    Inserts the `batch` of documents into collections.
    """
    mc = pymongo.MongoClient(uri)
    batch_total_population = 0
    batch_docs = 0
    try:
        # LAB - needs error handling
        with mc.start_session() as s:
            try:
                batch_total_population,batch_docs = write_batch(batch, mc, s)
            except Exception as exc:
                # Do something here!
                print("Error - what shall I do ??!??! {}".format(exc))
                raise

            q.put({"batch_pop": batch_total_population, "batch_docs": batch_docs })

    except Exception as e:
        print("Unexpected error found: {}".format(e))


def main(arguments):
    """
    Error Handling Lab
    ------------------
    1) Drops existing data
    2) Create the collections
    3) Starts transaction
    4) Imports dataset
    5) commits or aborts transaction
    """
    # get MongoDB URI
    uri = arguments['--uri']

    # drop dataset
    drop_dataset(uri)

    # create collections
    touch_collections(uri)

    # Process Comms Queue
    q = Queue(11)
    # Process list
    processes = []

    #import dataset in batches
    batch_size = 10
    with open(arguments['--file'], 'rt') as fd:
        for slice in iter(lambda: tuple(islice(fd, batch_size)),()):
            batch = list(map(json.loads, slice))
            processes.append( Process(target=load_data, args=(q, batch, uri)) )

    for p in processes:
        p.start()

    for p in reversed(processes):
        p.join()

    total_processed_population = 0
    total_processed_documents = 0
    while q.qsize() > 0:
        doc = q.get()
        total_processed_population += doc['batch_pop']
        total_processed_documents += doc['batch_docs']

    print("Documents Inserted: {0}".format(total_processed_documents))
    print("Total Population: {0}".format(total_processed_population))


if __name__ == '__main__':
    arguments = docopt(__doc__)
    main(arguments)
