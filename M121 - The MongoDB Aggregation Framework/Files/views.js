// this is the command we used to create the bronze_banking view in the database
// identical commands were used to create the silver and gold views, the only
// change was in the $match stage
db.createView("bronze_banking", "customers", [
  {
    "$match": { "accountType": "bronze" }
  },
  {
    "$project": {
      "_id": 0,
      "name": {
        "$concat": [
          { "$cond": [{ "$eq": ["$gender", "female"] }, "Miss", "Mr."] },
          " ",
          "$name.first",
          " ",
          "$name.last"
        ]
      },
      "phone": 1,
      "email": 1,
      "address": 1,
      "account_ending": { "$substr": ["$accountNumber", 7, -1] }
    }
  }
])

// getting all collections in a database and seeing their information
db.getCollectionInfos()

// getting information on views only
db.system.views.find()
