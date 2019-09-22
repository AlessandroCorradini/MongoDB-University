// first connection with mongo

use admin
db.shutdownServer()
exit

// second connection

db.serverStatus().connections
use admin
db.shutdownServer()
exit

// third connection

db.serverStatus().connections
