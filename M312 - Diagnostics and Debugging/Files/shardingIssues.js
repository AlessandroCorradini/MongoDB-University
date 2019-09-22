// first time connecting

sh.enableSharding("m312")
sh.shardCollection("m312.example", { last_name: 1, first_name: 1 } )
sh.status()

var oneMBString = Array(1000000).join(' ');
for (i=1; i<=100; i++) { db.example.insertOne( { last_name: "Cross", first_name: "Will", filler: oneMBString } ) }
sh.status()

for (i=1; i<=100; i++) { db.example.insertOne( { last_name: "Leite", first_name: "Norberto", filler: oneMBString } ) }
sh.status()

exit

// second time connecting

sh.splitAt( "m312.example", { last_name : "E", first_name: MinKey } )
sh.status()

sh.moveChunk("m312.example", { last_name: "Leite", first_name: "Norberto" }, "shard02")

use config
db.settings.save( { _id:"chunksize", value: 150 } )

sh.status()
    
var myStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
var myArray = myStr.split('');
for (i=0; i<26; i++) { sh.splitAt("m312.example", { last_name: myArray[i], first_name : MinKey } ) }
sh.status()

use m312
db.example.getShardDistribution()
