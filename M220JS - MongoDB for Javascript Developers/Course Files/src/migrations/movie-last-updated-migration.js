const MongoClient = require("mongodb").MongoClient
const ObjectId = require("mongodb").ObjectId
const MongoError = require("mongodb").MongoError

/**
 * Ticket: Migration
 *
 * Update all the documents in the `movies` collection, such that the
 * "lastupdated" field is stored as an ISODate() rather than a string.
 *
 * The Date.parse() method build into Javascript will prove very useful here!
 * Refer to http://mongodb.github.io/node-mongodb-native/3.1/tutorials/crud/#bulkwrite
 */

// This leading semicolon (;) is to make this Immediately Invoked Function Expression (IIFE).
// To read more about this type of expression, refer to https://developer.mozilla.org/en-US/docs/Glossary/IIFE
;(async () => {
  try {
    // ensure you update your host information below!
    const host = "mongodb://<your atlas connection uri from your .env file"
    const client = await MongoClient.connect(
      host,
      { useNewUrlParser: true },
    )
    const mflix = client.db(process.env.MFLIX_NS)

    // TODO: Create the proper predicate and projection
    // add a predicate that checks that the `lastupdated` field exists, and then
    // check that its type is a string
    // a projection is not required, but may help reduce the amount of data sent
    // over the wire!
    const predicate = { somefield: { $someOperator: true } }
    const projection = {}
    const cursor = await mflix
      .collection("movies")
      .find(predicate, projection)
      .toArray()
    const moviesToMigrate = cursor.map(({ _id, lastupdated }) => ({
      updateOne: {
        filter: { _id: ObjectId(_id) },
        update: {
          $set: { lastupdated: new Date(Date.parse(lastupdated)) },
        },
      },
    }))
    console.log(
      "\x1b[32m",
      `Found ${moviesToMigrate.length} documents to update`,
    )
    // TODO: Complete the BulkWrite statement below
    const { modifiedCount } = await "some bulk operation"

    console.log("\x1b[32m", `${modifiedCount} documents updated`)
    client.close()
    process.exit(0)
  } catch (e) {
    if (
      e instanceof MongoError &&
      e.message.slice(0, "Invalid Operation".length) === "Invalid Operation"
    ) {
      console.log("\x1b[32m", "No documents to update")
    } else {
      console.error("\x1b[31m", `Error during migration, ${e}`)
    }
    process.exit(1)
  }
})()
