// switch to the m201 database
use m201

// find all documents and sort them by ssn
db.people.find({}, { _id : 0, last_name: 1, first_name: 1, ssn: 1 }).sort({ ssn: 1 })

// create an explainable object for the people collection
var exp = db.people.explain('executionStats')

// and rerun the query (uses the index for sorting)
exp.find({}, { _id : 0, last_name: 1, first_name: 1, ssn: 1 }).sort({ ssn: 1 })

// this time, sort by first_name (didn't use the index for sorting)
exp.find({}, { _id : 0, last_name: 1, first_name: 1, ssn: 1 }).sort({ first_name: 1 })

// and rerun the first query, but sort descending (walks the index backward)
exp.find({}, { _id : 0, last_name: 1, first_name: 1, ssn: 1 }).sort({ ssn: -1 })

// filtering and sorting in the same query (both using the index, backward)
exp.find( { ssn : /^555/ }, { _id : 0, last_name: 1, first_name: 1, ssn: 1 } ).sort( { ssn : -1 } )

// drop all indexes
db.people.dropIndexes()

// create a new descending (instead of ascending) index on ssn
db.people.createIndex({ ssn: -1 })

// rerun the same query, now walking the index forward
exp.find( { ssn : /^555/ }, { _id : 0, last_name: 1, first_name: 1, ssn: 1 } ).sort( { ssn : -1 } )