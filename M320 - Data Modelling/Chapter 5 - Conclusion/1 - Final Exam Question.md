# Final Exam: Question 1

**Scenario**

Consider the following information about the operations on a system:

**Write Operations**

![](https://university-courses.s3.amazonaws.com/M320/m320-final-question1-writes.png)

- ID - A unique value to identify each operation.
- Description - A summary of the action occuring in the application that triggers this operation.
- Type - Additional information about whether this operation is an insert operation or an update operation.
- Durability - The number of nodes that must write the data to the database for the operation to be considered complete.
- Data Life - The amount of time this record will be stored in the database.
- Data Size - The number of bytes being written to the database.
- Storage Size - The amount of additional storage needed per day to store all new write data for this operation.
- Average Frequency - The average number of times this operation occurs per second.
- Peak Frequency - The average number of times this operation occurs per second at peak hour frequency.

**Read operations**

![](https://university-courses.s3.amazonaws.com/M320/m320-final-question1-reads.png)

- ID - A unique value to identify each operation.
- Description - A summary of the action occuring in the application that triggers this operation.
- Type - Additional information about whether this operation is a real-time operation or an analytics operation.
- Max Latency - The maximum acceptable amount of time for the application to wait to receive data from the database.
- Execution Time - The average amount of time the operation takes to complete. This is only filled in if the amount of time is longer than 1 second.
- Single Doc Size - The average size of the document being retrieved.
- Average Frequency - The average number of times this operation occurs per second.
- Peak Frequency - The average number of times this operation occurs per second at peak hour frequency.

Which of the following operations is the one that should be considered most prominently when designing our schema?



- W2 - application records time and user info when an item is viewed.
- W3 - user adds item to cart.
- R1 - user logs into the application.
- **R2 - user views a specific item.**
- R4 - user views their cart.