package mflix.api.daos;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class MigrationTest extends TicketTest {

  MongoCollection<Document> movies;

  @Before
  public void setup() throws IOException {
    String mongoUri = getProperty("spring.mongodb.uri");
    movies = MongoClients.create(mongoUri).getDatabase("sample_mflix").getCollection("movies");
  }

  @Test
  public void testAllDocumentsUpdateDateIsDateType() {
    Bson filter = Filters.type("lastupdated", "string");

    int expectedCount = 0;
    Assert.assertEquals(
        "Should not find documents where `lastupdated` is of " + "`string` type",
        expectedCount,
        movies.countDocuments(filter));
  }

  @Test
  public void testAllDocumentsIMDBRatingNumber() {
    Bson filter = Filters.not(Filters.type("imdb.rating", "number"));

    int expectedCount = 0;
    Assert.assertEquals(
        "Should not find documents where `imdb.rating` is of" + " not of `number` type",
        expectedCount,
        movies.countDocuments(filter));
  }
}
