#!/bin/sh


# create and list our intended data folder
mkdir -p /data/redaction
sudo chown -R `whoami` /data/redaction
ls /data/redaction

# boot up our mongod instance with no redaction enabled
mongod --dbpath /data/redaction --logpath /data/redaction/mongod.log

# set slowms to -1
mongo --eval "db.setProfilingLevel( 0, -1)"

# insert first document with sensitive
mongo redact --eval "db.sensitive.insert({'secret': 'This should not be found in the logs'})"

# look for the log entry with the inserted document
cat /data/redaction/mongod.log | grep "This should not be found in the logs"

# enable redact client log data
mongo admin --eval "db.adminCommand({ setParameter: 1, redactClientLogData: 1})"

# let's insert a document that should not be logged
mongo redact --eval "db.sensitive.insert({'secret': 'really, do not show this in the logs'})"

# look for that message in the log file
cat /data/redaction/mongod.log | grep "really, do not show this in the logs"
