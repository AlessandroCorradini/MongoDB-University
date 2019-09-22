// first time connecting
db.foo.insertOne( { hello: 'world' }, { writeConcern: { w: 3, wtimeout:1 } } );
db.foo.insertOne( { hello: 'world' }, { writeConcern: { w: 3, wtimeout:1 } } );
db.foo.insertOne( { hello: 'world' }, { writeConcern: { w: 3, wtimeout:1 } } );
db.foo.count();

db = connect( "m312:27001/admin" );
db.shutdownServer();

db = connect( "m312:27000/test" );
db.foo.insertOne( { hello: 'world' }, { writeConcern: { w: 3, wtimeout: 1 } } );
db.foo.insertOne( { hello: 'world' }, { writeConcern: { w: 4, wtimeout: 1 } } );
exit;

// second time connecting
var cur = db.friends.find( { email: /eh/ } )._addSpecial("$maxTimeMS", 1);
cur.forEach( function(x){ printjson(x)});
