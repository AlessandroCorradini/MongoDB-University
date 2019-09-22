# Final: Question 2

Consider a collection of phones called phones, used by a phone manufacturer to keep track of the phones currently in production.

Each document in phones looks like this:

```
{
  "model": 5,
  "date_issued" : ISODate("2016-07-27T20:27:52.834Z"),
  "software_version": 4.8,
  "needs_to_update": false
}
```

There is an update required for phones with software_version earlier than 4.0. Anyone still using a version older than 4.0 will be asked to update.

The phone manufacturer wants to set the flag needs_to_update to true when the value of software_version is lower than 4.0. For example, a document like this one:

```
{
  "model": 5,
  "date_issued" : ISODate("2014-03-04T14:23:43.123Z"),
  "software_version": 3.7,
  "needs_to_update": false
}
```

Should be updated to look like this:

```
{
  "model": 5,
  "date_issued" : ISODate("2014-03-04T14:23:43.123Z"),
  "software_version": 3.7,
  "needs_to_update": true
}
```

Which of the following update statements will correctly perform this update?




 - 
    ```
    phones.updateMany( { software_version: { "$lte": 4.0 } },
                        { "$set": { needs_to_update: True } } )
    ```
 - 
    ```
    phones.updateOne( { software_version: { "$lt": 4.0 } },
                      { "$set": { needs_to_update: True } } )
    ```
 - [X]
    ```
    phones.updateMany( { software_version: { "$lt": 4.0 } },
                        { "$set": { needs_to_update: True } } )
    ```
 - 
    ```
    phones.updateMany( { software_version: { "$gt": 4.0 } },
                        { "$set": { needs_to_update: True } } )
    ```
 - 
    ```
    phones.updateMany( { software_version: { "$lt": 4.0 } },
                        { "$inc": { needs_to_update: True } } )
    ```