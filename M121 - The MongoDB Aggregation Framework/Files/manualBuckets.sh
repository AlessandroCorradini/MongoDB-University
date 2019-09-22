#!/bin/sh

# create manual buckets using $ bucket
mongo startups --eval '
db.companies.aggregate( [
  { "$match": {"founded_year": {"$gt": 1980}, "number_of_employees": {"$ne": null}}  },
  {"$bucket": {
     "groupBy": "$number_of_employees",
     "boundaries": [ 0, 20, 50, 100, 500, 1000, Infinity  ]}
}] )
'

# reproduce error message for non matching documents
mongo startups --eval '
db.coll.insert({ x: "a" });
db.coll.aggregate([{ $bucket: {groupBy: "$x", boundaries: [0, 50, 100]}}])
'

# set `default` option to collect documents that do not match boundaries
mongo startups --eval '
db.companies.aggregate( [
  { "$match": {"founded_year": {"$gt": 1980}}},
  { "$bucket": {
    "groupBy": "$number_of_employees",
    "boundaries": [ 0, 20, 50, 100, 500, 1000, Infinity  ],
    "default": "Other" }
}] )
'

# reproduce error message for inconsitent boundaries datatype
mongo startups --eval '
db.coll.aggregate([{ $bucket: {groupBy: "$x", boundaries: ["a", "b", 100]}}])
'

# set `output` option for $bucket stage
mongo startups --eval '
db.companies.aggregate([
  { "$match":
    {"founded_year": {"$gt": 1980}}
  },
  { "$bucket": {
      "groupBy": "$number_of_employees",
      "boundaries": [ 0, 20, 50, 100, 500, 1000, Infinity  ],
      "default": "Other",
      "output": {
        "total": {"$sum":1},
        "average": {"$avg": "$number_of_employees" },
        "categories": {"$addToSet": "$category_code"}
      }
    }
  }
]
)
'
