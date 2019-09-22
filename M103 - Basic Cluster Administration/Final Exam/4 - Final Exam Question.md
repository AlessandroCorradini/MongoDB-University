# Final: Question 4

Given the following replica set configuration:

```
conf = {
  "_id": "replset",
  "version": 1,
  "protocolVersion": 1,
  "members": [
    {
      "_id": 0,
      "host": "192.168.103.100:27017",
      "priority": 2,
      "votes": 1
    },
    {
      "_id": 0,
      "host": "192.168.103.100:27018",
      "priority": 1,
      "votes": 1
    },
    {
      "_id": 2,
      "host": "192.168.103.100:27018",
      "priority": 1,
      "votes": 1
    }
  ]
}
```

What errors are present in the above replica set configuration?



- **You cannot specify two members with the same _id.**
- **You cannot specify the same host information among multiple members.**
- You cannot have three members in a replica set.
- You can only specify a priority of 0 or 1, member "_id": 0 is incorrectly configured.