package mflix.lessons.utils;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Updates.set;

public class ChangeStreamsInsertAndUpdate implements Runnable {

  private final MongoCollection<Document> collection;

  public ChangeStreamsInsertAndUpdate(MongoCollection<Document> collection) {
    this.collection = collection;
  }

  public void run() {
    insertAndUpdate();
  }

  private void insertAndUpdate() {
    sleepyTime();
    int i = 0;
    while (i < 10) {
      Document doc = new Document();
      doc.append("i", i++);
      doc.append("even", i % 2);
      collection.insertOne(doc);
    }
    int j = 0;
    while (j < 10) {
      Bson queryFilter = new Document("i", j);
      collection.updateOne(queryFilter, set("i", j * 10));
      j++;
    }
    Document stopDoc = new Document("stop", false);
    stopDoc.append("even", 1);
    collection.insertOne(stopDoc);
    collection.updateOne(stopDoc, set("even", 0));
    collection.updateOne(stopDoc, set("stop", true));
  }

  private void sleepyTime() {
    try {
      Thread.sleep(500);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
