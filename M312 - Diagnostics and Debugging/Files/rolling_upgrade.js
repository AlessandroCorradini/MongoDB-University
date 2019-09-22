// kill the server on port 30002 from the primary:
Mongo("localhost:30002").getDB("admin").shutdownServer();
rs.status().members[2]  // see that it's down
exit

// Connected to standalone, temporarily on port 40002, to create an index
db;
show collections();
db.people.createIndex({ssn:1});  // This is why we shut it down and restarted it, so we could do this on the standalone.
use admin;
db.shutdownServer();
exit;  // Time to restart it as a replica set member

// Connect to the primary when the standalone is back in the set.
rs.status();
Mongo("localhost:30001").getDB("admin").shutdownServer();  // shut down the other secondary
rs.status().members[1];  // see that it's down
exit


// Connect to the other standalone, temporarily on port 40001, to create the index there
db;
show collections();
db.people.createIndex({ssn:1});
use admin;
db.shutdownServer();
exit;


// When it gets back, we connect to the primary and check on it, then step down the Primary and shut it down.
rs.status().members[1];
rs.stepDown();
rs.status().members[0];  // No longer the primary
db.shutdownServer();

// Back on the server on port 40000, but now as a standalone.
db;
show collections();
db.people.createIndex({ssn:1});
use admin;
db.shutdownServer();
exit;

// See the replica set status now that all of the servers are back, this time with indexes.
rs.status().members;

