// switch to the m201 database
use m201

// create a collection-level collation for Portuguese
db.createCollection( "foreign_text", {collation: {locale: "pt"}})

// insert an example document
db.foreign_text.insert({ "name": "Máximo", "text": "Bom dia minha gente!"})

// explain the following query (uses the Portuguese collation)
db.foreign_text.find({ _id: {$exists:1 } } ).explain()

// specify an Italian collation for a find query
db.foreign_text.find({ _id: {$exists:1 } }).collation({locale: 'it'})

// specify a Spanish collation for an aggregation query
db.foreign_text.aggregate([ {$match: { _id: {$exists:1 }  }}], {collation: {locale: 'es'}})

// create an index with a collation that differs from the collection collation
db.foreign_text.createIndex( {name: 1},  {collation: {locale: 'it'}} )

// uses the collection collation (Portuguese)
db.foreign_text.find( {name: 'Máximo'}).explain()

// uses the index collation (Italian)
db.foreign_text.find( {name: 'Máximo'}).collation({locale: 'it'}).explain()


// create a case-insensitive index via collations
db.createCollection( "no_sensitivity", {collation: {locale: 'en', strength: 1}})

// insert some documents
db.no_sensitivity.insert({name: 'aaaaa'})
db.no_sensitivity.insert({name: 'aAAaa'})
db.no_sensitivity.insert({name: 'AaAaa'})

// sort them by name ascending
db.no_sensitivity.find().sort({name:1})

// even if we change the sort-order, the documents will be returned in the same
// order because of the case-insensitive collation
db.no_sensitivity.find().sort({name:-1})