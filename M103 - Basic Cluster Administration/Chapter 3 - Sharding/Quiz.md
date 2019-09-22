# Quiz

## When to Shard

Which of the following scenarios drives us to shard our cluster?



- When we start a new project with MongoDB.
- When our server disks are full.
- **When we reach the most powerful servers available, maximizing our vertical scale options.**
- **When holding more than 5TB per server and operational costs increase dramatically.**
- **Data sovereignty laws require data to be located in a specific geography.**

## Sharding Architecture

What is true about the primary shard in a cluster?



- **Non-sharded collections are placed on the primary shard.**
- The primary shard always has more data than the other shards.
- **The role of primary shard is subject to change.**
- Client applications communicate directly with the primary shard.
- **Shard merges are performed by the mongos.**

## Setting Up a Sharded Cluster

What is true about the mongos?



- **The mongos configuration file doesn't need to have a dbpath.**
- Users must be created on mongos when auth is enabled.
- **The mongos configuration file needs to specify the config servers.**
- The mongos configuration file doesn't need to have a port.
- The config server configuration files need to specify mongos.

## Config DB

When should you manually write data to the Config DB?



- When sharding a collection
- When removing a shard
- When importing a new dataset
- **When directed to by MongoDB documentation or Support Engineers**
- When adding a shard

## Shard Keys

True or False: Shard keys are mutable.



- **False**
- True

## Picking a Good Shard Key

Which of the following are indicators that a field or fields are a good shard key choice?



- **High Cardinality**
- **Low Frequency**
- Monotonic change
- Indexed
- **Non-monotonic change**

## Hashed Shard Keys

Which of the following functions does Hashed Sharding support?



- Fast sorts on the shard key
- **Even distribution of a monotonically changing shard key field**
- Targeted queries on a range of shard key values
- Even distribution of a monotonically changing shard key field in a compound index

## Chunks

What is true about chunks?



- **Increasing the maximum chunk size can help eliminate jumbo chunks.**
- Documents in the same chunk may live on different shards.
- **Chunk ranges have an inclusive minimum and an exclusive maximum.**
- Jumbo chunks can be migrated between shards.
- Chunk ranges can never change once they are set.

## Balancing

Given a sharded cluster running MongoDB 3.6, which of the shard components is responsible for running the Balancer process?



- Primary of each Shard Replica Set
- **Primary node of the Config Server Replica Set**
- Secondary of the Config Server Replica Set
- Mongos

## Queries in a Sharded Cluster

For a find() operation, which cluster component is responsible for merging the query results?



- A randomly chosen shard in the cluster
- The primary member of each shard
- **The mongos that issued the query**
- None, the results are coming out in the right order from the shards
- The primary member of the config server replica set

## Routed Queries vs Scatter Gather: Part 2

Given the following shard key, which of the following queries results in a targeted query?

```
{ "sku" : 1, "name" : 1 }
```



- db.products.find( { "name" : "MongoHacker" } )
- **db.products.find( { "sku" : 1337 } )**
- **db.products.find( { "sku" : 1337, "name" : "MongoHacker" } )**
- **db.products.find( { "name" : "MongoHacker", "sku" : 1337 } )**
