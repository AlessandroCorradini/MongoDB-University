db.movieDetails.find({runtime: {$gt: 90}})

db.movieDetails.find({runtime: {$gt: 90}}, {_id: 0, title: 1, runtime: 1})

db.movieDetails.find({runtime: {$gt: 90, $lt: 120}}, {_id: 0, title: 1, runtime: 1})

db.movieDetails.find({runtime: {$gte: 90, $lte: 120}}, {_id: 0, title: 1, runtime: 1})

db.movieDetails.find({runtime: {$gte: 180}, "tomato.meter": 100}, {_id: 0, title: 1, runtime: 1})

db.movieDetails.find({rated: {$ne: "UNRATED"}}, {_id: 0, title: 1, rated: 1})

db.movieDetails.find({rated: {$in: ["G", "PG"]}}, {_id: 0, title: 1, rated: 1})

db.movieDetails.find({rated: {$in: ["G", "PG", "PG-13"]}}, {_id: 0, title: 1, rated: 1}).pretty()

db.movieDetails.find({rated: {$in: ["R", "PG-13"]}}, {_id: 0, title: 1, rated: 1}).pretty()
