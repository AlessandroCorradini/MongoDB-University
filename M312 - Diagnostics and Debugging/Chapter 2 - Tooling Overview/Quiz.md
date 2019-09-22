# Quiz

## Introduction to Diagnostic and Debugging Tools

If you want tools to help you parse and visualize the log files, which of the following would be the best place to look?

- mongoreplay
- mongostat
- mongooplog
- **mtools**
- mongotop

## Introducing Server Logs

Which of the following are best learned from the server logs?

- **How many connections your server has**
- **Which of your queries have been long-running**
- Which indexes you have

## Introducing the Mongo Shell

What is the mongo shell?

- **A Javascript interpreter capable of running scripts**
- **An administrative interface for the MongoDB server process**

## currentOp and killOp

db.currentOp() is useful for finding which of the following?

- **Any long-running operations on the server**
- Connections that have been open for a long time
- Operations that are individually short-lived, but which are hammering the server and causing performance issues

## Introducing Server Status, Part 2

Can I suppress information from the serverStatus command output?

- No, serverStatus is an administration command therefore it's not possible to suppress information.
- **I can, by specifying in the command syntax which output document fields to omit.**

## Introducing the Profiler

You have a three-member replica set, capturing time-series data and serving queries. When you turn on the profiler, you find that the application immediately sees high response time on its queries.

Why does profiling interact poorly with this high-volume production workload?

**Turn off profiling, now!**

## Overview: Server Diagnostic Tools

From the following list, which tool(s) can I use to know, in real time, the number of queries / second occurring in a given replica set?

- **mongostat**
- mongoperf
- mongotop
- mongoreplay

## Overview: Mtools

Which of the following are tools included in the mtools package?

- mongostat
- **mgenerate**
- **mlogvis**
- **mplotqueries**

## Performance Statistics

Which of the following are performance metrics available in MongoDB Compass?

- **Number of Reads & Writes**
- Disk Space Usage
- **Network Activity**
- **Memory Usage**
- **Number of Operations (Insert, Queries, Updates, etc)**

## $indexStats, Part 5

You have a sharded cluster with 4 shards. The products collection is sharded on { name : 1, dateCreated : 1 }. There are 4 chunks, one on each server, and here is the range of the shard key:

```
s1: { name: MinKey, dateCreated: MinKey}  - { name: "e", dateCreated: MinKey}
s2: { name: "e", dateCreated: MinKey}  - { name: "g", dateCreated: MinKey}
s3: { name: "g", dateCreated: MinKey}  - { name: "m", dateCreated: MinKey}
s4:{ name: "m", dateCreated: MinKey}  - { name: MaxKey, dateCreated: MaxKey}
```

You have the following indexes:

```
{ _id: 1 }
{ name: 1, dateCreated: 1 }
{ price : 1 }
{ category: 1 }
```

You perform the following query:

```
db.products.find( { name : { $in : [ "iphone", "ipad", "apple watch" ] } } )
```

Which of the shards will increment their value of $indexStats for an index as a result of this query?

- **s1**
- s2
- **s3**
- s4
