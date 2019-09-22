#!/bin/sh

# render several different facets using $facet stage
mongo startups --eval 'db.companies.aggregate( [
    {"$match": { "$text": {"$search": "Databases"} } },
    { "$facet": {
      "Categories": [{"$sortByCount": "$category_code"}],
      "Employees": [
        { "$match": {"founded_year": {"$gt": 1980}}},
        {"$bucket": {
          "groupBy": "$number_of_employees",
          "boundaries": [ 0, 20, 50, 100, 500, 1000, Infinity  ],
          "default": "Other"
        }}],
      "Founded": [
        { "$match": {"offices.city": "New York" }},
        {"$bucketAuto": {
          "groupBy": "$founded_year",
          "buckets": 5   }
        }
      ]
  }}]).pretty()
'
