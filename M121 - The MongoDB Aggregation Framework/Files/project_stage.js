// project ``name`` and remove ``_id``
db.solarSystem.aggregate([{ "$project": { "_id": 0, "name": 1 } }]);

// project ``name`` and ``gravity`` fields, including default ``_id``
db.solarSystem.aggregate([{ "$project": { "name": 1, "gravity": 1 } }]);

// using dot-notation to express the projection fields
db.solarSystem.aggregate([{ "$project": { "_id": 0, "name": 1, "gravity.value": 1 } }]);

// reassing ``gravity`` field with value from ``gravity.value`` embeded field
db.solarSystem.aggregate([{"$project": { "_id": 0, "name": 1, "gravity": "$gravity.value" }}]);

// creating a document new field ``surfaceGravity``
db.solarSystem.aggregate([{"$project": { "_id": 0, "name": 1, "surfaceGravity": "$gravity.value" }}]);

// creating a new field ``myWeight`` using expressions
db.solarSystem.aggregate([{"$project": { "_id": 0, "name": 1, "myWeight": { "$multiply": [ { "$divide": [ "$gravity.value", 9.8 ] }, 86 ] } }}]);
