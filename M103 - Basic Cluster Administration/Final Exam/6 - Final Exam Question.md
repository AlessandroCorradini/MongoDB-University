# Final: Question 6

Given the following shard key:

```
{ "country": 1, "_id": 1 }
```

Which of the following queries will be routed (targeted)? Remember that queries may be routed to more than one shard.



- **db.customers.find({"_id": 914, "country": "Sweden"})**
- **db.customers.find({"country": "Norway", "_id": 54})**
- **db.customers.find({"country": { $gte: "Portugal", $lte: "Spain" }})**
- db.customers.find({"_id": 455})
