// familiarizing with the air_alliances schema
db.air_alliances.findOne()

// familiarizing with the air_airlines schema
db.air_airlines.findOne()

// performing a lookup, joining air_alliances with air_airlines and replacing
// the current airlines information with the new values
db.air_alliances
  .aggregate([
    {
      "$lookup": {
        "from": "air_airlines",
        "localField": "airlines",
        "foreignField": "name",
        "as": "airlines"
      }
    }
  ])
  .pretty()
