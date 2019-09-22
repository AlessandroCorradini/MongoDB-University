# Quiz

## Timeouts, Part 3

wtimeout can be set by the application to guarantee that:

- MongoDB will roll back the write operations if the nodes do not acknowledge the write operation within the specified wtimeout.
- **If a write is acknowledged, the replica set has applied the write on a number of servers within the specified wtimeout.**
- The application will learn how many servers have performed the write within wtimeout.

## Closing and Dropping Connections, Part 4

Which of the following will affect the number of available incoming connections a mongod instance will allow to be established?

- **OS ulimits**
- **net.maxIncomingConnections configuration file parameter**
- 65536 is the max number of connections, no other limit will apply

## Write Concern and Timeouts, Part 3

For a 7-member replica set with one arbiter and one delayed secondary, how many members are needed in order to acknowledge { w : "majority" } before the wtimeout is hit?

- 1
- 2
- 3
- **4**
- 5
- 6
- 7

## Hostnames and Cluster Configuration, Part 3

When connecting to a MongoDB Cluster, I should guarantee that:

- **The application should use more than one replica set member in the connection string**
- We can set any NIC or hostname for our replica set configuration set, since MongoDB will figure things out for us.
- **All mongos's and replica set members are addressable by the client and application hosts**

## Sharding Issues

Assuming the default chunk size, which of the following is/are jumbo chunks?

- 10 MB
- 20 MB
- 50 MB
- **100 MB**
- **200 MB**
- **500 MB**
