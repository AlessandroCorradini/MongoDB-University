// switch to the m201 database
use m201

// insert a restaurant document
db.restaurants.insert({
   "name" : "Han Dynasty",
   "cuisine" : "Sichuan",
   "stars" : 4.4,
   "address" : {
      "street" : "90 3rd Ave",
      "city" : "New York",
      "state" : "NY",
      "zipcode" : "10003"
   }
});

// and run a find query on city and cuisine
db.restaurants.find({'address.city': 'New York', 'cuisine': 'Sichuan'})

// create an explainable object
var exp = db.restaurants.explain()

// and rerun the query
exp.find({'address.city': 'New York', cuisine: 'Sichuan'})

// create a partial index
db.restaurants.createIndex(
  { "address.city": 1, cuisine: 1 },
  { partialFilterExpression: { 'stars': { $gte: 3.5 } } }
)

// rerun the query (doesn't use the partial index)
db.restaurants.find({'address.city': 'New York', 'cuisine': 'Sichuan'})

// adding the stars predicate allows us to use the partial index
exp.find({'address.city': 'New York', cuisine: 'Sichuan', stars: { $gt: 4.0 }})