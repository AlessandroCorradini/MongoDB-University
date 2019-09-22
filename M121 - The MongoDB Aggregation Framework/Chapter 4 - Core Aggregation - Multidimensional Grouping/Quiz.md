# Quiz

## Facets: Single Facet Query

Which of the following aggregation pipelines are single facet queries?

```
[X]
[
  {"$match": { "$text": {"$search": "network"}}},
  {"$sortByCount": "$offices.city"},
]
```
```
[X]
[
  {"$unwind": "$offices"},
  {"$project": { "_id": "$name", "hq": "$offices.city"}},
  {"$sortByCount": "$hq"},
  {"$sort": {"_id":-1}},
  {"$limit": 100}
]
```
```
[
  {"$match": { "$text": {"$search": "network"}}},
  {"$unwind": "$offices"},
  {"$sort": {"_id":-1}}
]
```

## Facets: Manual Buckets

Assuming that field1 is composed of double values, ranging between 0 and Infinity, and field2 is of type string, which of the following stages are correct?

- {'$bucket': { 'groupBy': '$field1', 'boundaries': [ "a", 3, 5.5 ]}}
- {'$bucket': { 'groupBy': '$field1', 'boundaries': [ 0.4, Infinity ]}}
- **{'$bucket': { 'groupBy': '$field2', 'boundaries': [ "a", "asdas", "z" ], 'default': 'Others'}}**

## Facets: Auto Buckets

Auto Bucketing will ...

- **given a number of buckets, try to distribute documents evenly accross buckets.**
- **adhere bucket boundaries to a numerical series set by the granularity option.**
- randomly distributed documents accross arbitrarily defined bucket boundaries.
- count only documents that contain the groupBy field defined in the documents.

## Facets: Multiple Facets

Which of the following statement(s) apply to the $facet stage?

- **The $facet stage allows several sub-pipelines to be executed to produce multiple facets.**
- **The $facet stage allows the application to generate several different facets with one single database request.**
- The output of the individual $facet sub-pipelines can be shared using the expression $$FACET.$.
- We can only use facets stages ($sortByCount, $bucket and $bucketAuto) as sub-pipelines of $facet stage.
