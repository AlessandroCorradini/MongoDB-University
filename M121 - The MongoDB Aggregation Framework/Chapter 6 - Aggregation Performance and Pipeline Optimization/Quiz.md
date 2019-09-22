# Quiz

## Aggregation Performance

With regards to aggregation performance, which of the following are true?

- You can increase index usage by moving $match stages to the end of your pipeline
- Passing allowDiskUsage to your aggregation queries will seriously increase their performance
- **When $limit and $sort are close together a very performant top-k sort can be performed**
- **Transforming data in a pipeline stage prevents us from using indexes in the stages that follow**

## Aggregation Pipeline on a Sharded Cluster

What operators will cause a merge stage on the primary shard for a database?

- **$out**
- $group
- **$lookup**

## Pipeline Optimization - Part 2

Which of the following statements is/are true?

- **Causing a merge in a sharded deployment will cause all subsequent pipeline stages to be performed in the same location as the merge**
- **The Aggregation Framework will automatically reorder stages in certain conditions**
- **The query in a $match stage can be entirely covered by an index**
- **The Aggregation Framework can automatically project fields if the shape of the final document is only dependent upon those fields in the input document.**
