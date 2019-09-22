#!/usr/bin/env bash

# install mtools package
sudo pip install psutil
sudo pip install mtools
sudo pip install numpy

# `mloginfo` help
mloginfo --help | less

# `mlogvis` help
mlogvis --help | less

# `mlaunch` help
mlaunch --help | less

# `mgenerate` help
mgenerate --help | less

# launch replica set
mlaunch init --replicaset --nodes 3 --names M312

# connect to initialized replica set
mongo --host $(hostname):27017

# generate dataset
cat /shared/schema.json
mgenerate /shared/schema.json

# find generated document documents
echo ' db.mgendata.findOne() ' | mongo --quiet

# analyze the log data from `rs1`
mloginfo /home/vagrant/data/replset/rs1/mongod.log

# filter log entries on namespace `SAMPLEDB.SAMPLECOLL`
mlogfilter /home/vagrant/data/replset/rs1/mongod.log --namespace SAMPLEDB.SAMPLECOLL

# analyze using a graphical representation of the logs entries
mlogvis /home/vagrant/data/replset/rs1/mongod.log -o /shared/visualization.html --no-browser
