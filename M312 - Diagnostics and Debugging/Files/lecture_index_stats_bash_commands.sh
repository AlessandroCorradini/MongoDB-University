#!/usr/bin/env bash
# shell scripts used in $indexStats videos

mlaunch init --replicaset --name m312RS --wiredTigerCacheSizeGB 0.5 --dir /data/m312RS --port 30000 --host localhost --oplogSize 100
mongo --port 30000 m312  # connect to primary

# ... 

mongo --port 30001 m312  # connect to new primary


# kill it, and launch the sharded cluster 
mlaunch stop --dir /data/m312RS
rm -r /data/m312RS
mlaunch init --replicaset --sharded 4 --wiredTigerCacheSizeGB 0.5 --dir /data/m312SH --port 30000 --host localhost --oplogSize 10
mongo --port 30000 m312

# ... connected to shell

# generate sample userdata
mgenerate -n 10000 --port 30000 -d m312 -c users  /shared/mgenerate_userdata.json
mongo --port 30000 m312
