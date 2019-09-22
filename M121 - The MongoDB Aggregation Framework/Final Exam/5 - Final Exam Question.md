# Final: Question 5

Consider a company producing solar panels and looking for the next markets they want to target in the USA. We have a collection with all the major cities (more than 100,000 inhabitants) from all over the World with recorded number of sunny days for some of the last years.

A sample document looks like the following:

```
db.cities.findOne()
{
"_id": 10,
"city": "San Diego",
"region": "CA",
"country": "USA",
"sunnydays": [220, 232, 205, 211, 242, 270]
}
```

The collection also has these indexes:

```
db.cities.getIndexes()
[
{
  "v": 2,
  "key": {
    "_id": 1
  },
  "name": "_id_",
  "ns": "test.cities"
},
{
  "v": 2,
  "key": {
    "city": 1
  },
  "name": "city_1",
  "ns": "test.cities"
},
{
  "v": 2,
  "key": {
    "country": 1
  },
  "name": "country_1",
  "ns": "test.cities"
}
]
```

We would like to find the cities in the USA where the minimum number of sunny days is 200 and the average number of sunny days is at least 220. Lastly, we'd like to have the results sorted by the city's name. The matching documents may or may not have a different shape than the initial one.

We have the following query:

```
var pipeline = [
    {"$addFields": { "min": {"$min": "$sunnydays"}}},
    {"$addFields": { "mean": {"$avg": "$sunnydays" }}},
    {"$sort": {"city": 1}},
    {"$match": { "country": "USA", "min": {"$gte": 200}, "mean": {"$gte": 220}}},
]
db.cities.aggregate(pipeline)
```

However, this pipeline execution can be optimized!

Which of the following choices is still going to produce the expected results and likely improve the most the execution of this aggregation pipeline?

```
var pipeline = [
    {"$sort": {"city": 1}},
    {"$addFields": { "min": {"$min": "$sunnydays"}}},
    {"$match": { "country": "USA", "min": {"$gte": 200}}},
]
```
```
var pipeline = [
    {"$match": { "country": "USA"}},
    {"$sort": {"city": 1}},
    {"$addFields": { "min": {"$min": "$sunnydays"}}},
    {"$match": { "min": {"$gte": 200}, "mean": {"$gte": 220}}},
    {"$addFields": { "mean": {"$avg": "$sunnydays" }}},
]
```
```
var pipeline = [
    {"$sort": {"city": 1}},
    {"$addFields": { "min": {"$min": "$sunnydays"}}},
    {"$addFields": { "mean": {"$avg": "$sunnydays" }}},
    {"$match": { "country": "USA", "min": {"$gte": 200}, "mean": {"$gte": 220}}},
]
```
```
var pipeline = [
    {"$sort": {"city": 1}},
    {"$match": { "country": "USA"}},
    {"$addFields": { "min": {"$min": "$sunnydays"}}},
    {"$match": { "min": {"$gte": 200}, "mean": {"$gte": 220}}},
    {"$addFields": { "mean": {"$avg": "$sunnydays" }}},
]
```
```
[X]
var pipeline = [
    {"$match": { "country": "USA"}},
    {"$addFields": { "mean": {"$avg": "$sunnydays"}}},
    {"$match": { "mean": {"$gte": 220}, "sunnydays": {"$not": {"$lt": 200 }}}},
    {"$sort": {"city": 1}},
]
```