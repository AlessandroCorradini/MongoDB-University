package mflix.api.daos;

import com.mongodb.client.MongoClient;
import mflix.api.models.Critic;
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

import java.util.List;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserReportTest extends TicketTest {

  private CommentDao dao;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setUp() {
    this.dao = new CommentDao(mongoClient, databaseName);
  }

  @Test
  public void testMostActiveUserComments() {
    String mostActiveCommenter = "roger_ashton-griffiths@gameofthron.es";
    List<Critic> mostActive = this.dao.mostActiveCommenters();
    int expectedListSize = 20;
    Assert.assertEquals(
        "mostActiveComments() should return 20 results",
        expectedListSize,
        mostActive.size());

    Assert.assertEquals(
        "The top comments user email does not match. Check your mostActiveCommenters() method",
        mostActiveCommenter,
        mostActive.get(0).getId());

    int expectedNumComments = 331;
    Assert.assertEquals(
        "The top comments count does not match.",
        expectedNumComments,
        mostActive.get(0).getNumComments());
  }
}
