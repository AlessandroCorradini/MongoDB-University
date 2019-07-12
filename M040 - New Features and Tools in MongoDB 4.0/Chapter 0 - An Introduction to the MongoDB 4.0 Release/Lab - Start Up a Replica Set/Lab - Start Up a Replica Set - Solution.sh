#!/bin/bash

# Creates the directories for the servers.
mkdir -p /data/db/m040/repl/{1,2,3}

# Starts up the servers.
mongod -f /shared/rs1_linux.conf
mongod -f /shared/rs2_linux.conf
mongod -f /shared/rs3_linux.conf

# Initiates the replicaSet.
mongo --eval 'rs.initiate()'
mongo --eval 'rs.add("m040:27027");rs.add("m040:27037")'

# Gives some time to the replicaSet for configuration.
sleep 10

# Validates the lab.
mongo --quiet validate_lab1.js