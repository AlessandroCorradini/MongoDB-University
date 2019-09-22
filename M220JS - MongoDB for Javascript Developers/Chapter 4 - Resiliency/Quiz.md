# Quiz

## Connection Pooling

Which of the following are benefits of connection pooling?



- The connection pool will persist after the client is terminated.
- **New operations can be serviced with pre-existing connections, so a new connection doesn't have to be created each time.**
- **A large influx of operations can be handled more quickly with a pool of existing connections.**
- Multiple database clients can share a connection pool.

## Robust Client Configuration

When should you set a wtimeout?



- When our application is using a connection pool of 100 or more connections.
- **When our application is using a Write Concern more durable than w: 1.**
- When our application is issuing bulk operations in large batches.
- When our application is using a Read Concern more durable than "available".

## Error Handling

Given a replica set with 5 nodes which write concern will cause an error when writing?



- **7**
- 5
- 1
- majority
