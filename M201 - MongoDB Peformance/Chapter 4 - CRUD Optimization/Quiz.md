# Quiz

## Optimizing your CRUD Operations

**Problem:**

When building indexes to service your queries, which of the following is the general rule of thumb you should keep when ordering your index keys?

Note, use the following definitions to for this question:

- equality: indexed fields on which our queries will do equality matching
- range: indexed fields on which our queries will have a range condition
- sort: indexed fields on which our queries will sort on

- equality, range, sort
- sort, range, equality
- sort, equality, range
- **equality, sort, range**
- range, sort, equality
- range, equality, sort

## Covered Queries

**Problem:**

Given the following indexes:

```
{ _id: 1 }
{ name: 1, dob: 1 }
{ hair: 1, name: 1 }
```

Which of the following queries could be covered by one of the given indexes?

- db.example.find( { _id : 1117008 }, { _id : 0, name : 1, dob : 1 } )
- db.example.find( { name : { $in : [ "Alfred", "Bruce" ] } }, { name : 1, hair : 1 } )
- db.example.find( { name : { $in : [ "Bart", "Homer" ] } }, {_id : 0, hair : 1, name : 1} )
- **db.example.find( { name : { $in : [ "Bart", "Homer" ] } }, {_id : 0, dob : 1, name : 1} )**

## Regex Performance

**Problem:**

Given the following index:

```
> db.products.createIndex({ productName: 1 })
```

And the following query:

```
> db.products.find({ productName: /^Craftsman/ })
```

Which of the following are true?

- The query will need to do a collection scan.
- **The query will do an index scan.**
- The query will likely need to look at all index keys.
- The query would match a productName of "Screwdriver - Craftsman Brand"

## Insert Performance

**Problem:**

Which of the following decreases the write performance of your MongoDB cluster?

- **Adding indexes**
- **Increasing the number of members we acknowledge writes from**
- Upgrading to MongoDB 3.4

## Data Type Implications Part 2

**Problem:**

Why is it important to maintain the same data type for fields across different documents?

- **It helps to simplify the client application logic**
- It's just a best practice; all drivers will deal with data type issues by default
- Because it aligns well with cosmetic shapes of documents
- **To avoid application data consistency problems**

## Aggregation Performance

**Problem:**

With regards to aggregation performance, which of the following are true?

- You can increase index usage by moving $match stages to the end of your pipeline
- Passing allowDiskUsage to your aggregation queries will seriously increase their performance
- **When $limit and $sort are close together a very performant top-k sort can be performed**
- **Transforming data in a pipeline stage prevents us from using indexes in the stages that follow**
