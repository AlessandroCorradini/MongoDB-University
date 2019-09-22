// Performs insert, read, update, and delete queries.

db.getSisterDB("m312");
db.sampleWorkload.drop();
db.sampleWorkload.createIndex( { firstNumber : 1 } );  // for efficient queries
// Insert 2 numbers,
for (i=1; i<=100; i++) {
    db.sampleWorkload.insertOne( { firstNumber : (2*i-1), secondNumber : (2*i-1) } );
    db.sampleWorkload.insertOne( { firstNumber : (2*i), secondNumber : (2*i) } );
    // Generate pseudo-random number from 1 to 2*i
    numToFind = Math.floor(Math.random() * 2 * i + 1);
    db.sampleWorkload.findOne( { firstNumber : numToFind } );  // efficient
    db.sampleWorkload.findOne( { secondNumber : ((numToFind + 5) % (2*i)) } );  // slow
    db.sampleWorkload.updateOne( { firstNumber : ((numToFind + 10) % (2*i)) },
                                 { $set : { stringField : "string" } } );
    db.sampleWorkload.deleteOne( { firstNumber : ((numToFind + 15) % (2*i)) } );
};
db.sampleWorkload.drop();
