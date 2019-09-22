# Quiz

## What is Replication?

Which of the following are true about binary replication and statement-based replication?



Binary replication is more accurate than statement-based replication.
MongoDB uses statement-based replication, not binary replication.
Statement-based replication is platform independent.

## MongoDB Replica Set

Which of the following are true for replica sets in MongoDB?



We should always use arbiters.
Replica sets provide high availability.
We can have up to 50 voting members in a replica set.
Replica set members have a fixed role assigned.

## Setting Up a Replica Set

Which of the following is/are true about setting up a replica set?



- rs.initiate() must be run on every node in the replica set.
- **Enabling internal authentication in a replica set implicitly enables client authentication.**
- **When connecting to a replica set, the mongo shell will redirect the connection to the primary node.**
- All nodes in a replica set must be run on the same port.

## Replication Configuration Document

Which of the following fields are included in the replica set configuration document?



- **version**
- **_id**
- **members**

## Replication Commands

What information can be obtained from running rs.printReplicationInfo()?



- **The time of the latest entry in the oplog.**
- The last statement entered in the oplog.
- **The time of the earliest entry in the oplog.**
- The current primary in the replica set.
- The earliest statement entered in the oplog.

## Local DB: Part 2

Which of the following is true?



- You cannot write to the local database.
- The local database does not allow the creation of other collections.
- **The local database will not be replicated.**
- We should drop the oplog.rs collection from time to time to avoid it becoming too big.
- **The oplog.rs collection contains all operations that will be replicated.**

## Reconfiguring a Running Replica Set

What is true about hidden nodes?



- **Hidden nodes vote in elections.**
- Hidden nodes are a type of arbiter.
- Hidden nodes can become primary.
- Secondary nodes cannot become hidden nodes without going offline.
- **Hidden nodes replicate data.**

## Reads and Writes on a Replica Set

Which of the following is true about reading and writing from secondaries?



- **We have to run rs.slaveOk() before we can read from secondary nodes.**
- Running rs.slaveOk() allows us to read and write from secondaries.
- Connecting to the replica set will automatically connect to a secondary node.

## Failover and Elections

Which of the following is true about elections?



- All nodes have an equal chance to become primary.
- **Nodes with priority 0 cannot be elected primary.**
- **Nodes with higher priority are more likely to be elected primary.**
- Elections can take place anytime while the primary is available.

## Write Concerns: Part 2

Consider a 3-member replica set, where one secondary is offline. Which of the following write concern levels can still return successfully?



- online
- **majority**
- all
- 3

## Read Concerns

Which of the following read concerns only return data from write operations that have been committed to a majority of nodes?



- **linearizable**
- local
- **majority**
- available

## Read Preferences

Which of the following read preference options may result in stale data?



- **primaryPreferred**
- **secondaryPreferred**
- primary
- **nearest**
- **secondary**
