# Final: Question 7

Assume a collection called people_heights with documents that look like this:

```
{
  "name": "Ada",
  "height": 1.7
}
```

Which of the following queries will find only the 4th- and 5th-tallest people in the people_heights collection?




- db.people_heights.find().sort("height", -1).skip(5).limit(3)
- db.people_heights.find().sort("height", -1).limit(5).skip(3)
- **db.people_heights.find().sort("height", -1).skip(3).limit(2)**
- db.people_heights.find().sort("height", -1).skip(3).limit(5)