#!/usr/bin/env bash
mlaunch init --replicaset --name m312RS --wiredTigerCacheSizeGB 0.3 --hostname localhost --port 30000

echo "You will need 3 tabs open, each with a connection to the DB via a mongo shell. I'll open the first."

mongo --port 30000
