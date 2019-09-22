# Quiz

## Introduction to Replica Set Transactions

**Problem:**

Which of the following statements are true about MongoDB Replica Set Transactions?

- With transactions in MongoDB, schema design is no longer relevant.
- **Transactions work across Replica Set clusters.**
- Transactions only work with standalone mongod instances.
- Prior to transactions, MongoDB was not able to guarantee ACID operations over single document operations.

## Transaction Considerations

**Problem:**

Which of the following are true in the case of multi-document transaction operations?

- **WiredTiger can proactively evict snapshots and abort transactions if cache pressure thresholds are reached.**
- **If a transaction takes more than 60 seconds to complete, it will abort.**
- **In cases where there is a significant amount of write operations in one single transaction (more than 1000 changed documents), the transaction should be broken into smaller sets of operations.**
- You can only read up to 1000 documents in a transaction.

## Write Conflicts

**Problem:**

Which of the following statements are correct?

- **Reads can still occur while a write lock is taken.**
- All writes not in a transaction context will be applied before the transaction write lock are released.
- All writes not in a transaction context will fail while a transaction is taking place.
- Transaction writes cannot be retried.

## Abort vs Commit

**Problem:**

What can cause TransientTransactionError to occur?

- **Network failures**
- **WriteConflict errors**
- Stack overflow error
- Out-of-memory error
- Null pointer exception