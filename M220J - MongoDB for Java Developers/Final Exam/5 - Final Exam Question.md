# Final: Question 5

Given the following code, doing a bulkWrite to an empty collection called employees:

```
MongoClient mongoClient = MongoClients.create(URI);
MongoDatabase db = mongoClient.getDatabase(DATABASE);
MongoCollection employeesCollection =
                  db.getCollection("employees");

Document doc1 = new Document("_id", 11)
                      .append("name", "Edgar Martinez")
                      .append("salary", "8.5M");
Document doc2 = new Document("_id", 3)
                      .append("name", "Alex Rodriguez")
                      .append("salary", "18.3M");
Document doc3 = new Document("_id", 24)
                      .append("name", "Ken Griffey Jr.")
                      .append("salary", "12.4M");
Document doc4 = new Document("_id", 11)
                      .append("name", "David Bell")
                      .append("salary", "2.5M");
Document doc5 = new Document("_id", 19)
                      .append("name", "Jay Buhner")
                      .append("salary", "5.1M");

List<WriteModel> requests = Arrays.asList(
                              new InsertOneModel<>(doc1),
                              new InsertOneModel<>(doc2),
                              new InsertOneModel<>(doc3),
                              new InsertOneModel<>(doc4),
                              new InsertOneModel<>(doc5));
try {
    employeesCollection.bulkWrite(requests);
} catch (Exception e) {
    System.out.println("ERROR: " + e.toString());
}
```

Which of the insert operations in requests will succeed?



- **Insert of doc1**
- **Insert of doc2**
- **Insert of doc3**
- Insert of doc4
- Insert of doc5
