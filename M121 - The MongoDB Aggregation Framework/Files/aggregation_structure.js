// simple first example
db.solarSystem.aggregate([{
  "$match": {
    "atmosphericComposition": { "$in": [/O2/] },
    "meanTemperature": { $gte: -40, "$lte": 40 }
  }
}, {
  "$project": {
    "_id": 0,
    "name": 1,
    "hasMoons": { "$gt": ["$numberOfMoons", 0] }
  }
}], { "allowDiskUse": true});
