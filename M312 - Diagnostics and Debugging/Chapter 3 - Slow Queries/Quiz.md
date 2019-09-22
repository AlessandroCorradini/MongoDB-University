# Quiz

## Response Time Degredation, Part 3

Which of the following can improve an application's response time?

- **Having the correct set of indexes.**
- **Providing enough RAM for our working set**
- **Optimizing our query results**

## Throughput Drops, Part 4

Where is the first place to look when you notice a sudden drop in throughput?

- **The logs**
- mongotop
- mongooplog

## Impact of Application Changes, Part 2

Which of the following is not a usual constraint that would impact your data model for MongoDB?

- **compare your current application performance to benchmarked data.**
- **predict the normal behavior of your application.**
- correct future problems, like a crystal ball.

## Using Mtools to Find Slow Queries, Part 3

Which of the following is/are true?

- **Mtools has a command line tool to list the slow queries from a mongod log.**
- Mtools has a command line tool to list the slow queries from the profiler.
- **Mtools lets you see slow queries in a two dimensional plot.**
- Both mloginfo and mplotqueries have the ability to filter the slow queries per-collection, as well as other criteria.
- **Mtools is not officially supported by MongoDB.**

## Fixing Missing Indexes, Part 5

Which of the following are true?

- **If your system is under load, using a "rolling upgrade" is the recommended way to build an index.**
- If you have a heavy write load, it is recommended to build indexes in the foreground on the Primary.
- Compass does not help with indexes.
- **We can use Ops Manager to rebuild our indexes**
- mtools is another tool that can help you build indexes.
