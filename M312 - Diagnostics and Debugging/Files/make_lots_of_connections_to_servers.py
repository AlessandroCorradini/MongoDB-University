#!/usr/bin/env python
"""
Makes a lot of connections to a mongod.

Usage:
    ./make_lots_of_connections_to_servers.py [options]

Options:
    -h --help       Show this text.
    --host <host>   Host where the mongod is located [default: localhost]
    --port <port>   Port where the mongod is located [default: 27017]
    --replset <set> Replica set name [default: replset]
    -n <conns>      Number of connections to make to the mongod. [default: 100]
"""

from docopt import docopt
from pymongo import MongoClient
from multiprocessing import Process
from time import sleep

def create_a_connection(host='localhost', port=27017, replica_set='replset'):
    client = MongoClient(host=host, port=port, replicaSet=replica_set)
    sleep(60)  # Delete this line if you uncomment the following lines.
    # try:
    #     sleep(5)
    #     client.test.test.find_one()
    #     sleep(5)
    #     client.test.test.find_one()
    #     sleep(10)
    #     client.test.test.find_one()
    #     sleep(60)
    # except Exception, e:
    #     print ("I can handle this")

    return True


def main():
    opts = docopt(__doc__)
    host = opts['--host']
    port = int(opts['--port'])
    replica_set = opts['--replset']
    number_of_connections = int(opts['-n'])
    processes = []
    for i in xrange(number_of_connections):
        p = Process(target=create_a_connection,
                    kwargs={"host": host,"port": port,
                            "replica_set": replica_set})
        p.start()
        processes.append(p)


if __name__ == '__main__':
    main()
