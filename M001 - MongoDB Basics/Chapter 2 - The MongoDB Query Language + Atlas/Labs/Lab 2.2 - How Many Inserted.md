# Lab 2.2: How Many Inserted?

**Problem:**

If the collection ```video.myMovies``` is currently empty, how many documents would be inserted by the following call to ```insertMany()```.

```
db.myMovies.insertMany(
  [
    {
      "_id" : "tt0084726",
      "title" : "Star Trek II: The Wrath of Khan",
      "year" : 1982,
      "type" : "movie"
    },
    {
      "_id" : "tt0796366",
      "title" : "Star Trek",
      "year" : 2009,
      "type" : "movie"
    },
    {
      "_id" : "tt0084726",
      "title" : "Star Trek II: The Wrath of Khan",
      "year" : 1982,
      "type" : "movie"
    },
    {
      "_id" : "tt1408101",
      "title" : "Star Trek Into Darkness",
      "year" : 2013,
      "type" : "movie"
    },
    {
      "_id" : "tt0117731",
      "title" : "Star Trek: First Contact",
      "year" : 1996,
      "type" : "movie"
    }
  ],
  {
    ordered: false
  }
);
```

- 1
- 2
- 3
- **4**
- 5