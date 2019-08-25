package mflix.api.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import mflix.config.MongoDBConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ConnectionTest extends TicketTest {

  private MovieDao dao;

  private String mongoUri;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setup() throws IOException {
    mongoUri = getProperty("spring.mongodb.uri");
    this.dao = new MovieDao(mongoClient, databaseName);
  }

  @Test
  public void testMoviesCount() {
    long expected =   23539;
    Assert.assertEquals("Check your connection string", expected, dao.getMoviesCount());
  }

  @Test
  public void testConnectionFindsDatabase() {

    MongoClient mc = MongoClients.create(mongoUri);
    boolean found = false;
    for (String dbname : mc.listDatabaseNames()) {
      if (databaseName.equals(dbname)) {
        found = true;
        break;
      }
    }
    Assert.assertTrue(
        "We can connect to MongoDB, but couldn't find expected database. Check the restore step",
        found);
  }

  @Test
  public void testConnectionFindsCollections() {

    MongoClient mc = MongoClients.create(mongoUri);
    // needs to find at least these collections
    List<String> collectionNames = Arrays.asList("comments", "movies", "sessions", "users");

    int found = 0;
    for (String colName : mc.getDatabase(databaseName).listCollectionNames()) {

      if (collectionNames.contains(colName)) {
        found++;
      }
    }

    Assert.assertEquals(
        "Could not find all expected collections. Check your restore step",
        found,
        collectionNames.size());
  }
}
