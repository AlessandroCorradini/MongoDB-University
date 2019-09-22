#!/usr/bin/env bash

# check number of assertion errors
mongo --eval ' db.serverStatus().asserts '

# check locks
mongo --eval ' db.serverStatus().locks '

# check operation counters
mongo --eval ' db.serverStatus().opcounters '

# check replication operation counters
mongo --eval ' db.serverStatus().opcountersRepl '

# check operation latencies counters
mongo --eval ' db.serverStatus().opLatencies '

# check wiredTiger statistics
mongo --eval ' db.serverStatus().wiredTiger '

# check memory statistics
mongo --eval ' db.serverStatus().mem '

# check metrics
mongo --eval ' db.serverStatus().metrics '

# check metrics commands
mongo --eval ' db.serverStatus().metrics.commands '

# check metrics documents
mongo --eval ' db.serverStatus().metrics.document '
# how they relate with opcounters
mongo --eval ' db.serverStatus().opcounters '

# correlation between inserts and document inserted
mongo --eval ' db.serverStatus().opcounters.insert '
mongo --eval ' db.serverStatus().metrics.document.inserted '

# insert several documents
mongo --eval ' db.things.insertMany([
  { n: 1}, { n: 2}, { n: 3}, { n: 4}, { n: 5},
]) '

# check correlation after executing inserts
mongo --eval ' db.serverStatus().opcounters.insert '
mongo --eval ' db.serverStatus().metrics.document.inserted '

# correlation between updates and document updated
mongo --eval ' db.serverStatus().opcounters.update '
mongo --eval ' db.serverStatus().metrics.document.updated '

# update many
mongo --eval ' db.things.updateMany( { } , {$set: {someFlag: true}})
'
# correlation after executing update
mongo --eval ' db.serverStatus().opcounters.update '
mongo --eval ' db.serverStatus().metrics.document.updated '
