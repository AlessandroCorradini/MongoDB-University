# Quiz

## Cursor Methods and Aggregation Equivalents

Which of the following aggregation stages have equivalent cursor methods?



- **$sort**
- **$limit**
- **$skip**

## Basic Writes

Which of the following are valid methods to insert a new document into a collection using the Node.js driver?



- writeDocument
- **insertOne**
- **insertMany**
- **updateOne with upsert: true specified in the options.**

## Write Concerns

Which of the following Write Concerns are valid in a 3-node replica set?



- **w: 0**
- **w: 1**
- w: 4
- w: 5
- **w: majority**

## Basic Updates

Which of the following can be passed to the updateOne() and updateMany() commands?



- **An update operation**
- A read preference
- **A query predicate**
- An aggregation pipeline
- **An upsert flag**

## Basic Joins

Why did we use a let expression with expressive $lookup, when joining the comments and the movies collection?



- To store the output of the pipeline in the movie_comments field.
- **To use fields from the movies documents in the pipeline.**
- To use fields from the comments documents in the pipeline.
- To count the number of comments documents.

## Basic Deletes

Which of the following is true regarding deletes in the Node.js driver?



- deleteAll deletes all matching documents in a collection.
- **deleteOne deletes the first matching document in $natural order.**
- **deleteMany deletes multiple matching documents in one operation.**
- deleteSome deletes some matching documents in a collection.
