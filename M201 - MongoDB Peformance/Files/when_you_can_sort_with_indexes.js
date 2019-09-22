// confirm you still have an index on job, employer, last_name, & first_name
db.people.getIndexes()

// create an explainable object for the people collection
var exp = db.people.explain("executionStats")

// sort all documents using the verbatim index key pattern
exp.find({}).sort({ job: 1, employer: 1, last_name : 1, first_name : 1 })

// sort all documents using the first two fields of the index (uses the index)
exp.find({}).sort({ job: 1, employer: 1 })

// sort all documents, swapping employer and job (doesn't use the index)
exp.find({}).sort({ employer: 1, job: 1 })

// all of these queries can use the index
db.people.find({}).sort({ job: 1 })
db.people.find({}).sort({ job: 1, employer: 1 })
db.people.find({}).sort({ job: 1, employer: 1, last_name: 1 })

// will still use the index (for sorting)
exp.find({ email:"jenniferfreeman@hotmail.com" }).sort({ job: 1 })

// use the index for filtering and sorting
exp.find({ job: 'Graphic designer', employer: 'Wilson Ltd' }).sort({ last_name: 1 })

// doesn't follow an index prefix, and can't use the index for sorting, only filtering
exp.find({ job: 'Graphic designer' }).sort({ last_name: 1 })


// create a new compound index
db.coll.createIndex({ a: 1, b: -1, c: 1 })

// walk the index forward
db.coll.find().sort({ a: 1, b: -1, c: 1 })

// walk the index backward, by inverting the sort predicate
db.coll.find().sort({ a: -1, b: 1, c: -1 })

// all of these queries use the index for sorting
db.coll.find().sort({ a: 1 })
db.coll.find().sort({ a: 1, b: -1 })
db.coll.find().sort({ a: -1 })
db.coll.find().sort({ a: -1, b: 1 })

// uses the index for sorting
exp.find().sort({job: -1, employer: -1})

// sorting is done in-memory
exp.find().sort({job: -1, employer: 1})