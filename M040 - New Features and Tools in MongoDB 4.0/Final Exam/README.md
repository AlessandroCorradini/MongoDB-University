# Final Exam

## Final Exam: Replica Set Transactions

### Problem:

Consider the following set of operations executing in the mongo shell:

    use m040
    db.a.insert({_id: 1})
    s1 = db.getMongo().startSession()
    s2 = db.getMongo().startSession()
    s1.startTransaction()
    s2.startTransaction()
    s1.getDatabase("m040").a.find()
    s2.getDatabase("m040").a.find()
    s1.getDatabase("m040").a.update({_id:1}, {$set: {value: "s1"}})
    s1.getDatabase("m040").a.find() // line 10
    s2.getDatabase("m040").a.find() // line 11
    s2.getDatabase("m040").a.update({_id:1}, {$set: {value: "s2"}})
    s1.getDatabase("m040").a.find()
    s2.getDatabase("m040").a.find()
    s1.abortTransaction()
    s2.abortTransaction()
    s1.getDatabase("m040").a.find()
    s2.getDatabase("m040").a.find() // line 18

Which of the following is/are true?

 - The output of line 18 is
   {
     _id: 1,
     value: "s1"
   }
 - **WriteConflict occurs on line 12**
 - The output of line 11 is
   {
     _id: 1,
     value: "s2"
   }
 - **The output of line 10 is
   {
     _id: 1,
     value: "s1"
   }**

## Final Exam: Sharding

### Problem:

Which of the following are new capabilities added in MongoDB 4.0 for sharded cluster administration ?

 - Getting faster acknowledgements for writes with concern majority
 - Getting faster chunk migrations
 - **Aborting an operation running on many shards**
 - **Identifying slow queries in the mongos logs**

## Final Exam: Aggregation

### Problem:

Consider this document:

    {
      _id: 1,
      time: "10/01/2018"
    }

If we execute the following aggregation:

    db.collection.aggregate([
      {
        $addFields: {
          time: {
            $dateToParts: {
              date: {
                $dateFromString: {
                  dateString: "$time",
                  format: "%d/%m/%Y"
                }
              }
            }
          }
        }
      },
      {
        $project:{
          time: {
            $dateFromParts: {
              "month": {$add:[-13, "$time.month"]},
              "year": "$time.year"
            }
          }
        }
      }
    ])
	
Which of the following is/are true?

Pay attention to the format string in the $addFields stage

 - **The day in the date field is 1st of December.**
 - The aggregation command fails with an "Unknown format field error".
 - The aggregation command fails due to an out-of-bound date.
 - **The year on the date is 2016.**
 - The _id is not present in the output document.

## Final Exam: Atlas

### Problem:

Which of the following statements is/are true regarding features recently added to Atlas?

 - **Using the Atlas BI connector frees you from maintaining the process locally.**
 - Only AWS supports sharded clusters.
 - You must use the live migrator tool to migrate out of the free tier.
 - **In Azure, Atlas supports backups as local snapshots in the region in which the cluster is located.**
 - **Atlas allows to keep data in given regions to comply with data governance, like GDPR.**

## Final Exam: Upgrade & Downgrade

### Problem:

Which of the following statements is/are true regarding upgrades to MongoDB 4.0 or downgrades from 4.0?

 - You can upgrade directly from MongoDB 3.4 to MongoDB 4.0
 - The flag previousVersionCompatibility controls the ability to use the new 4.0 features
 - **You can downgrade a cluster without downtime**
 - **You can upgrade a cluster without downtime**