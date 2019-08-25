package mflix.lessons;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mflix.api.daos.TicketTest;
import org.bson.Document;

import java.io.IOException;

public abstract class AbstractLesson extends TicketTest {
  protected MongoDatabase db;
  protected MongoDatabase testDb;
  protected MongoCollection<Document> moviesCollection;

  public AbstractLesson() {
    try {
      String mongoUri = getProperty("spring.mongodb.uri");
      String databaseName = getProperty("spring.mongodb.database");
      db = MongoClients.create(mongoUri).getDatabase(databaseName);
      moviesCollection = db.getCollection("movies");
      testDb = MongoClients.create(mongoUri).getDatabase("testDb");

    } catch (IOException e) {
      this.db = null;
    }
  }
}
