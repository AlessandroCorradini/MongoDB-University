// use the m201 database
use m201

// create an explainable object
var exp = db.restaurants.explain("executionStats")

// run an explained query (COLLSCAN & in-memory sort)
exp.find({ "address.zipcode": { $gt: '50000' }, cuisine: 'Sushi' }).sort({ stars: -1 })

// create a naive index
db.restaurants.createIndex({"address.zipcode": 1,"cuisine": 1,"stars": 1})

// rerun the query (uses the index, but isn't very selective and still does an 
// in-memory sort)
exp.find({ "address.zipcode": { $gt: '50000' }, cuisine: 'Sushi' }).sort({ stars: -1 })

// see how many documents match 50000 for zipcode (10)
db.restaurants.find({ "address.zipcode": '50000' }).count()

// see how many documents match our range (about half)
db.restaurants.find({ "address.zipcode": { $gt: '50000' } }).count()

// see how many documents match an equality condition on cuisine (~2%)
db.restaurants.find({ cuisine: 'Sushi' }).count()

// reorder the index key pattern to be more selective
db.restaurants.createIndex({ "cuisine": 1, "address.zipcode": 1, "stars": 1 })

// and rerun the query (faster, still doing an in-memory sort)
exp.find({ "address.zipcode": { $gt: '50000' }, cuisine: 'Sushi' }).sort({ stars: -1 })

// swap stars and zipcode to prevent an in-memory sort
db.restaurants.createIndex({ "cuisine": 1, "stars": 1, "address.zipcode": 1 })

// awesome, no more in-memory sort! (uses the equality, sort, range rule)
exp.find({ "address.zipcode": { $gt: '50000' }, cuisine: 'Sushi' }).sort({ stars: -1 })