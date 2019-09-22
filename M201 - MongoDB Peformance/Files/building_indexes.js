// switch to the m201 database
use m201

// create an index in the background
db.restaurants.createIndex( {"cuisine": 1, "name": 1, "address.zipcode": 1}, {"background": true} )


// from another shell, switch to the m201 database, and find all index-build
// operations
use m201
db.currentOp(
  {
    $or: [
      { op: "command", "query.createIndexes": { $exists: true } },
      { op: "insert", ns: /\.system\.indexes\b/ }
    ]
  }
)

// kill the running query using the opid (it's not really 12345)
db.killOp(12345)