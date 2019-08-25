package mflix.api.daos;

import com.mongodb.ConnectionString;
import com.mongodb.WriteConcern;
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
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = {CommentDao.class, MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TimeoutsTest extends TicketTest {

  @Autowired MongoClient mongoClient;
  private String mongoUri;

  @Value("${spring.mongodb.database}")
  String databaseName;

  private MovieDao movieDao;

  @Before
  public void setUp() throws IOException {
    this.movieDao = new MovieDao(mongoClient, databaseName);
    mongoUri = getProperty("spring.mongodb.uri");
    mongoClient = MongoClients.create(mongoUri);
  }

  @Test
  public void testConfiguredWtimeout() {
    WriteConcern wc =
            this.movieDao.mongoClient.getDatabase(databaseName).getWriteConcern();

    Assert.assertNotNull(wc);
    int actual = wc.getWTimeout(TimeUnit.MILLISECONDS);
    int expected = 2500;
    Assert.assertEquals("Configured `wtimeoutms` not set has expected", expected, actual);
  }

  @Test
  public void testConfiguredConnectionTimeoutMs() {
    ConnectionString connectionString = new ConnectionString(mongoUri);
    int expected = 2000;
    int actual = connectionString.getConnectTimeout();

    Assert.assertEquals(
        "Configured `connectionTimeoutMS` does not match expected", expected, actual);
  }
}
