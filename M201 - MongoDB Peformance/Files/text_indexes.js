// switch to the m201 database
use m201

// insert 2 example documents
db.textExample.insertOne({ "statement": "MongoDB is the best" })
db.textExample.insertOne({ "statement": "MongoDB is the worst." })

// create a text index on "statement"
db.textExample.createIndex({ statement: "text" })

// Search for the phrase "MongoDB best"
db.textExample.find({ $text: { $search: "MongoDB best" } })

// Display each document with it's "textScore"
db.textExample.find({ $text: { $search : "MongoDB best" } }, { score: { $meta: "textScore" } })

// Sort the documents by their textScore so that the most relevant documents
// return first
db.textExample.find({ $text: { $search : "MongoDB best" } }, { score: { $meta: "textScore" } }).sort({ score: { $meta: "textScore" } })
