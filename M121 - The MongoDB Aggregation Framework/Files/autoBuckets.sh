#!/bin/sh

# generate buckets automatically with $bucktAuto stage
mongo startups --eval 'db.companies.aggregate([
  { "$match": {"offices.city": "New York" }},
  {"$bucketAuto": {
    "groupBy": "$founded_year",
    "buckets": 5
}}])
'

# set `output` option for $bucketAuto
mongo startups --eval 'db.companies.aggregate([
  { "$match": {"offices.city": "New York" }},
  {"$bucketAuto": {
    "groupBy": "$founded_year",
    "buckets": 5,
    "output": {
        "total": {"$sum":1},
        "average": {"$avg": "$number_of_employees" }  }}}
])
'

# default $buckeAuto behaviour
mongo startups --eval '
for(i=1; i <= 1000; i++) {  db.series.insert( {_id: i}  ) };
db.series.aggregate(
  {$bucketAuto:
    {groupBy: "$_id", buckets: 5 }
})
'

# generate automatic buckets using granularity numerical series R20
mongo startups --eval 'db.series.aggregate(
  {$bucketAuto:
    {groupBy: "$_id", buckets: 5 , granularity: "R20"}
  })
'
