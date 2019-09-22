#!/usr/bin/env bash

# NOTE: In this video, Will doesn't spin up his replica set using mlaunch.
# Technically, mtools hadn't been introduced yet, but you should still use it.
# I'm setting the variables he used; set them to match the setup from your system.

# create instance directory structure
mkdir -p /data/rs/p

# launch server
mongod -f /shared/primary.conf

# collect process id
processId=$(ps -ef | grep mongod | grep primary | awk '{print $2}')

# open logs file
less /data/rs/p/mongod.log

# fiter out all the CONTROL lines and timestamp for better reading
cat /data/rs/p/mongod.log | grep -v 'CONTROL' | sed "s/[^ ]* //"

# kill the server process
kill -9 $processId

# confirm that the process is down
ps -ef | grep mongod | grep primary

# restart the server
mongod -f /shared/primary.conf

# start server with max verbosity
mongod -vvvvv
