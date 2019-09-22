// Mongo shell scripts used in $indexStats videos
// first time connecting

db
db.people.insertOne( { _id : 0, last_name : "Cross", first_name : "Will" } )
db.people.insertOne( { _id : 1, last_name : "Leite", first_name : "Norberto" } )

db.people.createIndex( { last_name : 1, first_name : 1 } )
db.people.createIndex( { first_name : 1 } )

db.people.aggregate( [ { $indexStats : { } } ] ).pretty()

db.people.findOne ( { _id : 0 } )
db.people.aggregate( [
  { $indexStats : { } },
  { $project : { key: 1, "accesses.ops": 1 } }
] ).pretty()

db.people.findOne( { _id : null } ) 
db.people.aggregate( [
  { $indexStats : { } },
  { $project : { key: 1, "accesses.ops": 1 } }
] ).pretty()

db.people.find() 
db.people.aggregate( [
  { $indexStats : { } },
  { $project : { key: 1, "accesses.ops": 1 } }
] ).pretty()

db.people.aggregate( [ 
  { $indexStats : { } },
  { $project : { key: 1, "accesses.ops": 1, "accesses.since": 1 } }
] ).pretty()

for (i=1; i<=100; i++) {
    var curs = db.people.find( { last_name : { $gte : "C", $lt : "D" },
                                 first_name : { $gte : "W", $lt : "X" } } );
    curs.forEach( function(doc) { printjson(doc) } )
}
db.people.aggregate( [ { $indexStats : { } },
                       { $project : { key : 1, accesses: 1 } }
                     ] )
 
db.people.insertOne( { _id : 2, last_name : "Coupal", first_name : "Daniel" } )
db.people.aggregate( [ { $indexStats : { } },
                       { $project : { key : 1, "accesses.ops" : 1 } } ] )
 
db.people.updateOne(
  { last_name : "Cross", first_name : "Will" },
  {
    $set : { address : { street : "123 Fake St.",
                         city : "Jersey City",
                         state : "New Jersey",
                         comment : "You got a problem with that?" } }
  } )
db.people.aggregate( [ { $indexStats : { } },
                       { $project : { key: 1, "accesses.ops": 1 } } ]
                    ).pretty()

 
db.people.updateOne(
  { last_name : "Cross", first_name : "Will" },
  {
    $set : { address : { city : "Jersey City",
                         state : "New Jersey",
                         comment : "You got a problem with that?" } }
  } )
db.people.aggregate( [
  { $indexStats : { } },
  { $project : { key: 1, "accesses.ops": 1 } }
] ).pretty()

db.people.aggregate( [ { $indexStats : { } } ] ).pretty()

rs.stepDown()
exit

// ... connect to new primary

db.people.aggregate( [ { $indexStats : { } } ] ).pretty()
exit

// ... connect to sharded cluster

sh.enableSharding("m312")
db.users.createIndex( { "user.name.last": 1, "user.name.first": 1, "user.created_at": 1 } )
sh.shardCollection(
  "m312.users",
  { "user.name.last": 1, "user.name.first": 1, "user.created_at": 1 } )
var alphabet = [
  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
  "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
];
alphabet.forEach(function(chr) {
  sh.splitAt(
    "m312.users",
    {
      "user.name.last": chr,
      "user.name.first": MinKey,
      "user.created_at": MinKey
    } )
} );
sh.status()
sh.status()
exit

// ... load userdata and reconnect

db.users.aggregate( [
  { $indexStats : { } },
  { $project : { key : 1, "accesses.ops" : 1, host: 1 } }
] )

db.users.find( { "user.name.last": "Ward", "user.name.first": "Ryan" } )
db.users.aggregate( [
  { $indexStats : { } },
  { $project : { key : 1, "accesses.ops" : 1, host: 1 } }
] )


db.users.find( { "user.name.last": { $lte : "D" } } )
db.users.aggregate( [
  { $indexStats : { } },
  { $project : { key : 1, "accesses.ops" : 1, host: 1 } }
] )

db.users.createIndex( { "user.phone_no" : 1 } )
var phoneNumber = db.users.findOne().user.phone_no
db.users.find( { "user.phone_no": phoneNumber } ).count()
db.users.aggregate( [
  { $indexStats : { } },
  { $match : { key: { "user.phone_no" : 1 } } },
  { $project : { key : 1, "accesses.ops": 1, host: 1 } }
] )
