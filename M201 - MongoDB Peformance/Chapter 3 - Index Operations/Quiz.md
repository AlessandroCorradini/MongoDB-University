# Quiz

## Building Indexes

**Problem:**

Which of the following are true of index build operations?

- **Foreground index builds block all reads and writes to the collection being indexed.**
- **Foreground index builds block all reads and writes to the database that holds the collection being indexed.**
- Background index builds do not impact the query performance of the MongoDB deployment while running.
- **Background index builds take longer to complete than foreground index builds.**
- Background index builds block reads and writes to the collection being indexed.

## Query Plans

**Problem:**

Which of the following is/are true concerning query plans?

- MongoDB's query optimizer is statistically based, where collection heuristics are used to determine which plan wins.
- **Query plans are cached so that plans do not need to be generated and compared against each other every time a query is executed.**
- When query plans are generated, for a given query, every index generates at least one query plan.
- If an index can't be used, then there is no query plan for that query.

## Understanding Explain Part 2

**Problem:**

With the output of an explain command, what can you deduce?

- **The index used by the chosen plan**
- **If a sort was performed by walking the index or done in memory**
- All the available indexes for this collection
- **All the different stages the query needs to go through with details about the time it takes, the number of documents processed and - returned to the next stage in the pipeline**
- The estimation of the cardinalities of the distribution of the values

## Forcing Indexes with Hint

**Problem:**

What is the method that forces MongoDB to use a particular index?

- force()
- suggest()
- **hint()**
- index()

## Resource Allocation for Indexes Part 3

**Problem:**

Which of the following statements apply to index resource allocation?

- **For the fastest processing, we should ensure that our indexes fit entirely in RAM**
- Index information does not need to completely allocated in RAM since MongoDB only uses the right-end-side to the index b-tree, regardless of the queries that use index.
- **Indexes are not required to be entirely placed in RAM, however performance will be affected by constant disk access to retrieve index information.**

## Basic Benchmarking Part 2

**Problem:**

What type of strategy and tools should we be using to performance benchmark a MongoDB installation?

- **Publicly available tools, including correct database variations**
- Mongo shell to test read performance
- Test transfer ratio using mongodump
