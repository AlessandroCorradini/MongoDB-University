#!/usr/bin/env python
"""
Simulates a busy application executing queries on a regular basis

Usage:
    ./lab1_chapter4.py
"""

from pymongo import MongoClient
from multiprocessing import Process
from time import sleep
from string import ascii_lowercase
import random
import re

def create_a_connection(host, port, replset):
    client = MongoClient(host=host, port=port, replicaSet=replset)
    try:
        query = re.compile(random.choice(ascii_lowercase), re.IGNORECASE)
        client.m312.companies.find({"name": query})
        sleep(5)

    except Exception, e:
        print ("Exception caught, but I can handle this: {e}".format(e=e))

    while True:
        # noop
        sleep(30)

def main():
    processes = []
    print 'application running, open a new terminal window!'
    for i in range(0, 100):
        p = Process(target=create_a_connection,
                    kwargs= {"host": "m312", "port":30000,
                             "replset": "m312rs"})
        p.start()
        processes.append(p)

    while True:
        try:
            sleep(5)
            print 'spawning a new process'
            p = Process(target=create_a_connection,
                        kwargs= {"host":"m312", "port": 30000,
                                 "replset": "m312rs"})
            p.start()
            processes.append(p)

        except Exception, e:
            print "Exception caught: {e}".format(e=e)

if __name__ == '__main__':
    main()
