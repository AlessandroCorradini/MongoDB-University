# Final: Question 3

Given the following audit filter:

```
{
  "$or": [
    {
      "atype": "authCheck",
      "param.command": {
        "$in": [
          "find",
          "insert",
          "delete",
          "update",
          "findandmodify"
        ]
      }
    },
    {
      "atype": {
        "$in": [
          "createCollection",
          "dropCollection"
        ]
      }
    }
  ]
}
```

Which of the following commands would be logged by this audit filter?

Note: You can assume that auditAuthorizationSuccess is set to true.

- **db.products.findOne({product: 'Door Hinge'})**
- **db.products.insert({product: 'Amplifier'})**
- **db.products.find({product: 'Candle'})**
- show dbs
- **db.products.insertOne({product: 'Basket'})**
