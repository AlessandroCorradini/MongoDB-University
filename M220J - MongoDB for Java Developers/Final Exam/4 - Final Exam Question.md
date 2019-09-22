# Final: Question 4

Suppose a client application is sending writes to a replica set with 3 nodes:

![](https://s3.amazonaws.com/edu-static.mongodb.com/lessons/M220/notebook_assets/replica_set_primary_secondary_highlighted_ack.png)

Before returning an acknowledgement back to the client, the replica set waits.

When the write has been applied by the nodes marked in stripes, it returns an acknowledgement back to the client.

What Write Concern was used in this operation?



- w: available
- w: 0
- w: 1
- **w: majority**