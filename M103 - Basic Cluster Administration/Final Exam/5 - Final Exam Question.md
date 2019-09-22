# Final: Question 5

Given the following replica set configuration:

```
conf = {
  "_id": "replset",
  "version": 1,
  "protocolVersion": 1,
  "members": [
    {
      "_id": 0,
      "host": "localhost:27017",
      "priority": 1,
      "votes": 1
    },
    {
      "_id": 1,
      "host": "localhost:27018",
      "priority": 1,
      "votes": 1
    },
    {
      "_id": 2,
      "host": "localhost:27019",
      "priority": 1,
      "votes": 1
    },
    {
      "_id": 3,
      "host": "localhost:27020",
      "priority": 0,
      "votes": 0,
      "slaveDelay": 3600
    }
  ]
}
```

What is the most likely role served by the node with "_id": 3?



- **It serves as a "hot" backup of data in case of accidental data loss on the other members, like a DBA accidentally dropping the database.**
- It serves to service reads and writes for people in the same geographic region as the host machine.
- It serves as a hidden secondary available to use for non-critical analysis operations.
- It serves as a reference to perform analytics on how data is changing over time.