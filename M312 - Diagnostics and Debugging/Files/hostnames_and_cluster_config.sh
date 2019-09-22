#!/bin/sh

# use nodes-vagrant-env
cd nodes-vagrant-env
ls

# bring up the environment nodes
vagrant up

# check virtual machines status
vagrant status

# launch mongod on different nodes
vagrant ssh m1 -- -t 'mongod -f /shared/mongod.cnf'
vagrant ssh m2 -- -t 'mongod -f /shared/mongod.cnf'
vagrant ssh m3 -- -t 'mongod -f /shared/mongod.cnf'

mongo --host 192.168.15.101 --eval '
  rs.initiate({
    "_id": "M312",
    "members": [
      {"_id": 0, "host": "192.168.15.101"},
      {"_id": 1, "host": "m2.university.mongodb"},
      {"_id": 2, "host": "m3"},
    ]
  })
'

# check replica set status
mongo --host 192.168.15.101 --eval '
rs.status()
'

# connect to the replica set
mongo --host M312/192.168.15.101

# ping the missing m2.university.mongodb
ping m2.university.mongodb

# connect to m1 instance
vagrant ssh m1

# connect to replica set using m2.university.mongodb as seed member
mongo --host M312/m2.university.mongodb

# connect to replica set using m1 as seed member
mongo --host M312/m1

# check isMaster primary
mongo --host M312/m1 --eval '
rs.isMaster().primary
'

# check config members
mongo --host M312/m1 --eval '
rs.conf().members
'

# edit `/etc/hosts` file
sudo vi /etc/hosts

# append m2 hostname
sudo echo "192.168.15.102 m2 m2.university.mongodb" >> /etc/hosts

# connect to replica set primary
mongo --host M312/m2.university.mongodb

# connect to m1
vagrant ssh m1

# change hosts file
sudo sed -ie "s/m3//" /etc/hosts

# connect to local mongod
mongo --eval ' rs.status() '

# connect to the replica set
mongo --host M312/192.168.15.101

# step down primary
mongo --host M312/m2 --eval '
rs.stepDown()
'


# redo change on hosts file
sudo sed -ie "s/m3.university.mongodb/m3 m3.university.mongodb/" /etc/hosts
