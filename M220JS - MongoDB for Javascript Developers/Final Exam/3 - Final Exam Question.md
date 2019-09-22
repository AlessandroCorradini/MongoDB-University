# Final: Question 3

Suppose an instance of MongoClient is created with the following URI string:

```
import { MongoClient } from "mongodb"

const URI = "mongodb+srv://m220-user:m220-pass@m220-test.mongodb.net/test"

const testClient = await MongoClient.connect(
  URI,
  {
    authSource: "admin",
    connectTimeoutMS: 50,
    retryWrites: true,
    useNewUrlParser: true
  },
)

const clientOptions = testClient.s.options
```

The variable representing our client, testClient, will:



- **automatically retry writes that fail.**
- allow a maximum of 50 connections in the connection pool.
- **use SSL when connecting to MongoDB.**
- authenticate against the test database.
- **wait at most 50 milliseconds for timing out a connection.**
