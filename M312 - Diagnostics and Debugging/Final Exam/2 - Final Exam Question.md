# Final: Question 2

You have a replica set with the following parameters:

- All members have 1 vote
- Any delayed secondaries, if present, are delayed by 24 hours
- The wtimeout is set to 60 seconds (60,000 ms) for all writes
- Unless specified, all replica set members use the default settings in MongoDB 3.4.
- The replica set has 3 members
   - Including any delayed Secondaries
   - Including at most one Arbiter
- You are using a write concern of { w : "majority" } for all writes

You should also assume the following, in order to keep the problem simple:

- All parts of the system (the network, the servers, etc.) work as intended
- You will never lose enough servers to prevent a Primary from getting elected
- Any server can go down, and if it does, assume that it will stay down until any writes referenced in this problem have occurred on a Primary, and at least 60 seconds have passed.
- If a Primary goes down, a Secondary will be elected Primary in <10 seconds
   - Any choices offered apply only to writes performed after a new Primary is elected, if applicable.
"Any one server" can mean any single mongod server process: Primary, Secondary, Delayed Secondary, or Arbiter.

Which of the following is/are true?

- For a replica set with two standard data bearing members plus one delayed Secondary, the application can receive acknowledgment for writes if any one server is lost
- **For a replica set with three standard data-bearing, non-delayed members, the application can receive acknowledgment for writes if any one server is lost**
- For a replica set with two data bearing members and one Arbiter (no delayed Secondaries), the application can receive acknowledgment for writes if any one server is lost