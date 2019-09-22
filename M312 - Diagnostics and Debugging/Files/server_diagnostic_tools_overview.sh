#!/usr/bin/env bash

# download package
cd /shared
wget https://downloads.mongodb.com/linux/mongodb-linux-x86_64-enterprise-ubuntu1404-3.4.2.tgz

# unzip package content
tar xzvf mongodb-linux-x86_64-enterprise-ubuntu1404-3.4.2.tgz

# list package content
ls -R /shared/mongodb-linux-x86_64-enterprise-ubuntu1404-3.4.2/bin/

# launch some servers
cd ~
mlaunch init --replicaset --name m312RS --wiredTigerCacheSizeGB 0.25 --oplogSize 100 --port 30000 --host localhost

# launch mongoreplay
sudo mongoreplay monitor -i lo -e "port 30000"

# launch mongostat
mongostat --port 30000

# mongostat in discover mode
mongostat --port 30000 --discover

# launch mongotop
mongotop --port 30000
