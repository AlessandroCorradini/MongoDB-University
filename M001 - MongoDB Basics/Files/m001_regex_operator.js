db.movieDetails.find({}, {_id: 0, "title": 1, "awards.text": 1}).pretty()

db.movieDetails.find({"awards.text": {$regex: /^Won.* /}}, {_id: 0, title: 1, "awards.text": 1}).pretty()
