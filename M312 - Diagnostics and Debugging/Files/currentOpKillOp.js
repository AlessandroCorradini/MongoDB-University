function insertDocs() {
    db = db.getSisterDB("m312");
    db.things.drop();
    var bigString = Array(10000).join("Padding ");  // 80 kb string
    var docCount = 0;
    var docsPerBatch = 100;
    while (true) {
        print("About to insert docs. So far, I have inserted " + docCount +
              " documents.");
        docs = [];
        for (i=1; i<=docsPerBatch; i++) {
            docs.push( { docNumber : (docCount + i), filler : bigString } );
        };
        db.things.insertMany( docs );
        docCount += docsPerBatch;
        print("  ... done.");
    };
};

function queryDocs() {
    try {
        db = db.getSisterDB("m312");
        var curs = db.things.find( { filler: /asdf/ } );
        curs.next();
    } catch (err) { 
        print(err.message);
    }
};

function currentOpKillOp() { 
    db = db.getSisterDB("m312");
    x = db.currentOp();
    opDocs = [];
    x.inprog.forEach(
        function(opDoc) {
            print("Found op: " + opDoc.op + " running for " + opDoc.secs_running + "seconds.");
            if ((opDoc.op == "query") && (opDoc.secs_running > 1)) {
                opDocs.push(opDoc);
            };
        });
    if (opDocs.length == 0) {  // no long-running operations
        print("Didn't find a long running query. Are you sure you " +
              "are running the 'queryDocs()' function in another " +
              "shell instance?");
    } else if (opDocs.length > 1) {  // 2+ long-running operations
        for (op in opDocs) {
            printjson(opDocs[op]);
        };
        print("I found two or more long running query operations. " +
              "I was only expecting one. I've printed them; " +
              "see above.");
    } else if (opDocs.length == 1) {
        opDoc = opDocs[0]
        printjson(opDoc);
        print("Found your long running query operation; see above.");
        print("It has been running for " + opDoc.secs_running +
              " seconds.");
        print("Its opid is " + opDoc.opid + ". Go kill it!");
    };
};
