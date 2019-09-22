#!/usr/bin/env bash

# First, from your host machine:

vagrant ssh

# Next, launch the replica set, and initialize:

mlaunch init --replicaset --name m312RS --wiredTigerCacheSizeGB 0.3 --port 30000 --host localhost
cd /shared
mongoimport -d m312 -c people --port 30000 people.json
echo "db.people.createIndex( { last_name: 1, first_name: 1 } )" | mongo --port 30000 m312
mongo --port 30000 m312

# Next, go to a secondary
mongo --port 30001 m312

# Go back to the primary to rebuild the ssn index in the background.
mongo --port 30000 m312

# See the logPath:
logPath=$(ps -ef | grep mongo | grep 30000 | awk '{ print $14 }')
echo $logPath
mlogfilter $logPath --component INDEX  # get the INDEX component of the log

mlogfilter $logPath --component COMMAND  # See the index creation command itself
