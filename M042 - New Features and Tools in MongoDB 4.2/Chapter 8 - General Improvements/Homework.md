# Homework

## Homework: Updates Using Aggregation Pipeline

Which of the following is a valid update that uses an aggregation pipeline?

Choose the best answer:

- [X]
    ```
    db.assignments.update(
    { "is_late": true },
    [ { "$set": { "score": { "$subtract": [ "$score", 10 ] } } } ]
    )
    ```
- 
    ```
    db.assignments.update(
    { "is_late": true },
    { "$set": { "score": 0 } }
    )
    ```
- 
    ```
    db.assignments.update(
    [ { "$set": { "score": { "$subtract": [ "$score", 10 ] } } } ]
    )
    ```