# Quiz

## $match: Filtering documents

Which of the following is/are true of the $match stage?

- $match can use both query operators and aggregation expressions.
- $match can only filter documents on one field.
- **It uses the familiar MongoDB query language.**
- **It should come very early in an aggregation pipeline.**

## Shaping documents with $project

Which of the following statements are true of the $project stage?

- **Once we specify a field to retain or perform some computation in a $project stage, we must specify all fields we wish to retain. The only exception to this is the _id field.**
- **Beyond simply removing and retaining fields, $project lets us add new fields.**
- $project can only be used once within an Aggregation pipeline.
- $project cannot be used to assign new values to existing fields.

## Optional Lab - Expressions with $project

Let's find how many movies in our movies collection are a "labor of love", where the same person appears in cast, directors, and writers

Note that you may have a dataset that has duplicate entries for some films. Don't worry if you count them few times, meaning you should not try to find those duplicates.

To get a count after you have defined your pipeline, there are two simple methods.

```
// add the $count stage to the end of your pipeline
// you will learn about this stage shortly!
db.movies.aggregate([
  {$stage1},
  {$stage2},
  ...$stageN,
  { $count: "labors of love" }
])

// or use itcount()
db.movies.aggregate([
  {$stage1},
  {$stage2},
  {...$stageN},
]).itcount()
```

How many movies are "labors of love"?

- 1259
- 1595
- 1263
- **1597**
