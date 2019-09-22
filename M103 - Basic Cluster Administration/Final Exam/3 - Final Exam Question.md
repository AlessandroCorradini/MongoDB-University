# Final: Question 3

Given the following output from rs.status().members:

```
[
  {
    "_id": 0,
    "name": "localhost:27017",
    "health": 1,
    "state": 1,
    "stateStr": "PRIMARY",
    "uptime": 548,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "electionTime": Timestamp(1521038358, 2),
    "electionDate": ISODate("2018-03-14T14:39:18Z"),
    "configVersion": 2,
    "self": true
  },
  {
    "_id": 1,
    "name": "localhost:27018",
    "health": 1,
    "state": 2,
    "stateStr": "SECONDARY",
    "uptime": 289,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDurable": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "optimeDurableDate": ISODate("2018-03-14T14:47:51Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.558Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:56.517Z"),
    "pingMs": NumberLong("0"),
    "syncingTo": "localhost:27022",
    "configVersion": 2
  },
  {
    "_id": 2,
    "name": "localhost:27019",
    "health": 1,
    "state": 2,
    "stateStr": "SECONDARY",
    "uptime": 289,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDurable": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "optimeDurableDate": ISODate("2018-03-14T14:47:51Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.558Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:56.654Z"),
    "pingMs": NumberLong("0"),
    "syncingTo": "localhost:27022",
    "configVersion": 2
  },
  {
    "_id": 3,
    "name": "localhost:27020",
    "health": 1,
    "state": 2,
    "stateStr": "SECONDARY",
    "uptime": 289,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDurable": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "optimeDurableDate": ISODate("2018-03-14T14:47:51Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.558Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:56.726Z"),
    "pingMs": NumberLong("0"),
    "syncingTo": "localhost:27022",
    "configVersion": 2
  },
  {
    "_id": 4,
    "name": "localhost:27021",
    "health": 0,
    "state": 8,
    "stateStr": "(not reachable/healthy)",
    "uptime": 0,
    "optime": {
      "ts": Timestamp(0, 0),
      "t": NumberLong("-1")
    },
    "optimeDurable": {
      "ts": Timestamp(0, 0),
      "t": NumberLong("-1")
    },
    "optimeDate": ISODate("1970-01-01T00:00:00Z"),
    "optimeDurableDate": ISODate("1970-01-01T00:00:00Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.656Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:12.668Z"),
    "pingMs": NumberLong("0"),
    "lastHeartbeatMessage": "Connection refused",
    "configVersion": -1
  },
  {
    "_id": 5,
    "name": "localhost:27022",
    "health": 1,
    "state": 2,
    "stateStr": "SECONDARY",
    "uptime": 289,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDurable": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "optimeDurableDate": ISODate("2018-03-14T14:47:51Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.558Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:55.974Z"),
    "pingMs": NumberLong("0"),
    "syncingTo": "localhost:27017",
    "configVersion": 2
  },
  {
    "_id": 6,
    "name": "localhost:27023",
    "health": 1,
    "state": 2,
    "stateStr": "SECONDARY",
    "uptime": 289,
    "optime": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDurable": {
      "ts": Timestamp(1521038871, 1),
      "t": NumberLong("1")
    },
    "optimeDate": ISODate("2018-03-14T14:47:51Z"),
    "optimeDurableDate": ISODate("2018-03-14T14:47:51Z"),
    "lastHeartbeat": ISODate("2018-03-14T14:47:56.558Z"),
    "lastHeartbeatRecv": ISODate("2018-03-14T14:47:56.801Z"),
    "pingMs": NumberLong("0"),
    "syncingTo": "localhost:27022",
    "configVersion": 2
  }
]
```

At this moment, how many replica set members are eligible to become primary in the event of the current Primary crashing or stepping down?



- 6
- **5**
- 4
- 7
