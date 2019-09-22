# Quiz

## The Mongod

When specifying the --fork argument to mongod, what must also be specified?



- --logdestination
- --logfile
- --loglocation
- **--logpath**

## MongoDB Architecture

Sharded clusters are composed of:



- Load Balancer
- **Mongos**
- **Config Servers**
- **Replica Sets**

## Data Structures

Which of the following is true about indexes?



- **Indexes take up space in memory.**
- By default, MongoDB doesn't create any indexes.
- **Indexes speed up our read operations.**
- Indexes speed up our write operations.

## Configuration File

Consider the following:

```
mongod --dbpath /data/db --logpath /data/logs --replSet M103 --bind_ip '127.0.0.1,192.168.0.100' --keyFile /data/keyfile --fork
```

Which of the following represents a configuration file equivalent to the command line options?



- 
    ```
    storage
    dbPath="/data/db"
    systemLog
    destination="/data/logs"
    replication
    replSetName="M103"
    net
    bindIp="127.0.0.1,192.168.0.100"
    security
    keyFile="/data/keyfile"
    processManagement
    fork=true
    ```
- 
    ```
    storage.dbPath: /data/db
    systemLog.destination: "/data/logs"
    replication.replSetName: "M103"
    net.bindIp: "127.0.0.1,192.168.0.100"
    security.keyFile: "/data/keyfile"
    processManagement.fork: true
    ```
- [X]
    ```
    storage:
    dbPath: "/data/db"
    systemLog:
    destination: file
    path: "/data/logs"
    replication:
    replSetName: "M103"
    net:
    bindIp: "127.0.0.1,192.168.0.100"
    security:
    keyFile: "/data/keyfile"
    processManagement:
    fork: true
    ```

## File Structure

Which of the following files in the MongoDB data directory can you access to view collection data?



- The collection.wt file
- The storage.bson file
- The WiredTiger.wt file
- **None of the above**

## Basic Commands

Which of the following methods executes a database command?



- db.runThisCommand( { <COMMAND> } )
- **db.runCommand( { <COMMAND> } )**
- db.executeCommand( { <COMMAND> } )
- db.command( { <COMMAND> } )

## Logging Basics

Which of the following process logging components will capture the following operation, assuming a verbosity of 1 or greater?

```
db.runCommand(
  {
    update: "products",
    updates: [
      {
        q: <query>,
        u: <update>,
      }
  }
)
```



- UPDATE
- **WRITE**
- QUERY
- **COMMAND**

## Profiling the Database

What events are captured by the profiler?



- **Administrative commands**
- **Cluster configuration operations**
- **CRUD operations**
- WiredTiger storage data
- Network timeouts

## Basic Security: Part 2

When should you deploy a MongoDB deployment with security enabled?



- **When deploying an evaluation environment**
- **When deploying your production environment**
- **When deploying your staging environment**
- **When deploying a development environment**

## Built-In Roles: Part 2

Which of the following are Built-in Roles in MongoDB?



- dbAdminAllDatabases
- **read**
- **dbAdminAnyDatabase**
- readWriteUpdate
- rootAll

## Server Tools Overview

Which of the following are true differences between mongoexport and mongodump?



- **Mongodump outputs BSON, but mongoexport outputs JSON.**
- **Mongodump can create a data file and a metadata file, but mongoexport just creates a data file.**
- Mongoexport outputs BSON, but mongodump outputs JSON.
- Mongoexport is typically faster than mongodump.
- **By default, mongoexport sends output to standard output, but mongodump writes to a file.**