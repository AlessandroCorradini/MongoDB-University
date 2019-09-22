# Homework: Wildcard Index

Consider the following index:

```
db.authors.createIndex(
  { "books.$**": 1 },
  { "wildcardProjection": { "books.page_count": 0 } }
)
```

Which of the following queries will utilize this index, assuming it is the only existing index on the collection?


- [X]
    ```
    db.authors.find(
    { "books.genre": { "$in": [ "Fantasy", "Science Fiction" ] } }
    )
    ```
- 
    ```
    db.authors.find(
    { "rating": { "$lt": 3 } }
    )
    ```
- 
    ```
    db.authors.find(
    { "books.page_count": { "$gt": 200 } }
    )
    ```
