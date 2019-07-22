# Final Exam

## Final Exam: Question 1

**Problem:**

Connect to our class Atlas cluster from Compass and view the ```citibike.trips``` collection. Use the schema view and any filters you feel are necessary to determine the range of values for the ```usertype``` field. Which of the following are values found in this collection for the field ```usertype```?

- **"Subscriber"**
- "Anonymous"
- **"Customer"**
- "Pay As You Go"
- null

## Final Exam: Question 2

**Problem:**

Connect to our class Atlas cluster from Compass and view the ```100YWeatherSmall.data``` collection. Using the Schema view, explore the ```wind``` field. The ```wind``` field has the value type of ```document```. Which of the following best describes the schema of this embedded document?

- Two fields -- both with the value type "document"
- Two fields -- one field with the value type "string", one with a value type of "document"
- **Three fields -- two with the value type "document", one with the value type "string"**
- Three fields -- two with the value type "string", one with the value type of "int32"
- Three fields -- all with the value type "document"

## Final Exam: Question 3

**Problem:**

Connect to the M001 class Atlas cluster from Compass and view the ```100YWeatherSmall.data``` collection. What is the value type of the "wind.speed.rate" field?

- int32
- array
- **double**
- string
- document

## Final Exam: Question 4

**Problem:**

Please connect to the M001 class Atlas cluster. You may answer this question using either the mongo shell or Compass.

For this question we will use the ```citibike``` database.

How many documents in the ```citibike.trips``` collection have the key ```tripduration``` set to null? Ignore any documents that do not contain the ```tripduration``` key.

- **2**
- 3
- 11
- 47
- 114

## Final Exam: Question 5

**Problem:**

Using the ```video.movieDetails``` collection, which of the queries below would produce output documents that resemble the following. Check all that apply.

```
{ "title" : "P.S. I Love You" }
{ "title" : "Love Actually" }
{ "title" : "Shakespeare in Love" }
```

NOTE: We are not asking you to consider specifically which documents would be output from the queries below, but rather what fields the output documents would contain.

- db.movieDetails.find({}, {title})
- **db.movieDetails.find({}, {title: 1, _id: 0})**
- **db.movieDetails.find({year: 1964}, {title: 1, _id: 0})**
- db.movieDetails.find({}, {title: 1})
- db.movieDetails.find({title: "Muppets from Space"}, {title: 1})
- db.movieDetails.find({title: ""}, {title: 1})

## Final Exam: Question 6

**Problem:**

Please connect to the M001 class Atlas cluster from the mongo shell or Compass and view the ```video.movies``` collection. How many movies match the following criteria?

- The cast includes either of the following actors: "Jack Nicholson", "John Huston".
- The viewerRating is greater than 7.
- The mpaaRating is "R".

- 1
- 5
- **8**
- 19
- 26