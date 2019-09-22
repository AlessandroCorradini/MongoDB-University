// sort some shapes
db.shapes.find({}, {base:1, _id:0}).sort({base:1})

// create an index with a numeric ordering collation
db.shapes.createIndex({base: 1}, {collation: {locale: 'en', numericOrdering: true}})

// now the sort will be in numeric order grouped for each data type
db.shapes.find({}, {base:1, _id:0}).sort({base:1})