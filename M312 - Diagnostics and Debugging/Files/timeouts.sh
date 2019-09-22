#!/usr/bin/env bash

mlaunch init --name TIMEOUTS --replicaset --nodes 3 --dir timeouts --port 27000

mongo --port 27000  # first time connecting

mongoimport --host TIMEOUTS/m312:27000 -d test -c friends /dataset/friends_1000000.json

mongo --port 27000  # second time connecting
