# Quiz

## Sharding: Logging of Slow Queries and Sharded Kill on mongos

**Problem:**

Which of the following are true about the sharded kill command?

- **Allows administrators to kill operations in the mongos that spawn into individual shards**
- Allows administrators to kill sessions only on the mongod on which it is run
- Kills all cursors currently open in the mongos on which it is run
- Kills all the mongos processes of the sharded cluster
- All of the above

## Sharding: Latency improvements on Secondary Reads

**Problem:**

Which of the following statements are true as a result of the replication improvements in MongoDB 4.0?

- **Writes with a concern of majority on the Primary are acknowledged faster**
- **Reads on Secondaries are not blocked while Oplog entries are applied**
- You need to modify your application code to benefit from the improvements