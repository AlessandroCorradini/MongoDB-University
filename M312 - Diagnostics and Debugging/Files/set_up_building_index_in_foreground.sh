#!/usr/bin/env bash

killall mongod
wait
rm -r ~/data
mlaunch init --dir ~/data/ --replicaset --name m312RS --port 30000 --host localhost --wiredTigerCacheSizeGB 0.3
echo "cfg = rs.conf(); cfg.members[0].priority = 3; cfg.members[1].priority = 2; rs.reconfig(cfg, { force : true })" | mongo --port 30000
sleep 20
mongoimport --host m312RS/m312:30000,m312:30001,m312:30002 --drop -d m312 -c employees /shared/employees.json
