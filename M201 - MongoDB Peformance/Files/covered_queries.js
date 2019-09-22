// use the m201 database
use m201

// create an explainable object
var exp = db.restaurants.explain("executionStats")

// create a compound index on three fields
db.restaurants.createIndex({name: 1, cuisine: 1, stars: 1})

// checkout a projected query
db.restaurants.find({name: { $gt: 'L' }, cuisine: 'Sushi', stars: { $gte: 4.0 } }, { _id: 0, name: 1, cuisine: 1, stars: 1 })

// and look at it's explain output (it's covered, no docs examined)
exp.find({name: { $gt: 'L' }, cuisine: 'Sushi', stars: { $gte: 4.0 } }, { _id: 0, name: 1, cuisine: 1, stars: 1 })


// get the same output as the first query
db.restaurants.find({name: { $gt: 'L' }, cuisine: 'Sushi', stars: { $gte: 4.0 } }, { _id: 0, address: 0 })

// but when looking at the explain output we see that it's not a covered query
exp.find({name: { $gt: 'L' }, cuisine: 'Sushi', stars: { $gte: 4.0 } }, { _id: 0, address: 0 })