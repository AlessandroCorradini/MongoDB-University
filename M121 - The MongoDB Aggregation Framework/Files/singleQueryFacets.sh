#!/bin/sh

# find one company document
mongo startups --eval '
db.companies.findOne()
'

# create text index
mongo startups --eval '
db.companies.createIndex({"description": "text", "overview": "text"})
'

# find companies matching term `networking` using text search
mongo startups --eval '
db.companies.aggregate([
  {"$match": { "$text": {"$search": "network"}  }  }] )
'

# $sortByCount single query facet for the previous search
mongo startups --eval '
db.companies.aggregate([
  {"$match": { "$text": {"$search": "network"}  }  },
  {"$sortByCount": "$category_code"}] )
'

# extend the pipeline for a more elaborate facet
mongo startups --eval '
db.companies.aggregate([
  {"$match": { "$text": {"$search": "network"}  }  } ,
  {"$unwind": "$offices"},
  {"$match": { "offices.city": {"$ne": ""}  }}   ,
  {"$sortByCount": "$offices.city"}] )
'
