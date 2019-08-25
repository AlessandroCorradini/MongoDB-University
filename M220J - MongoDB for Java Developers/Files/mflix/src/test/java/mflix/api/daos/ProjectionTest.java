package mflix.api.daos;

import com.mongodb.client.MongoClient;
import mflix.config.MongoDBConfiguration;
import org.bson.Document;
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

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectionTest extends TicketTest {

  private MovieDao dao;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setup() {
    this.dao = new MovieDao(mongoClient, databaseName);
  }

  @Test
  public void testFindMoviesByCountry() {
    int expectedSize = 2;
    String country = "Kosovo";
    Iterable<Document> cursor = dao.getMoviesByCountry(country);
    int actualSize = 0;
    for (Document d : cursor) {
      System.out.println(d);
      actualSize++;
    }

    Assert.assertEquals(
        "Unexpected number of returned movie documents. Check your query filter",
        expectedSize,
        actualSize);
  }

  @Test
  public void testProjectionShape() {
    Iterable<Document> cursor = dao.getMoviesByCountry("Russia", "Japan");
    for (Document doc : cursor) {
      Assert.assertEquals(
          "Document should have only two fields. Check your projection", 2, doc.keySet().size());

      Assert.assertTrue(
          "Should return `_id` field. Check your projection",
           doc.keySet().contains("_id"));
      Assert.assertTrue(
          "Should return `title` field. Check your projection",
           doc.keySet().contains("title"));
    }
  }
}
