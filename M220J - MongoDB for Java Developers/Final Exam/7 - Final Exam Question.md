# Final: Question 7

Assume a collection called people_heights with documents that look like this:

```
{
  "name": "Ada",
  "height": 1.7
}
```

Which of the following queries will find only the 4th- and 5th-tallest people in the people_heights collection represented by the coll object?



- oll.find().sort(orderBy(descending("height"))).skip(5).limit(3)
- **coll.find().sort(orderBy(descending("height"))).skip(3).limit(2)**
- coll.find().sort(orderBy(descending("height"))).limit(5).skip(3)
- coll.find().sort(orderBy(descending("height"))).skip(3).limit(5)