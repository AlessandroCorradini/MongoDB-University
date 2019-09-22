#!/usr/bin/env bash

mlaunch init --replicaset --sharded 3 --wiredTigerCacheSizeGB 0.3 --oplogSize 10 --port 30000 --host localhost --dir /data/m312
mongo --port 30000 m312  # first time connecting

mlogfilter /data/m312/shard01/rs1/mongod.log --component SHARDING | less

mongo --port 30000 m312  # second time connecting

