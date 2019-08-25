package mflix.api.daos;

import com.mongodb.client.MongoClient;
import mflix.api.models.User;
import mflix.config.MongoDBConfiguration;
import org.bson.Document;
import org.junit.After;
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

import java.util.HashMap;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserPreferencesTest extends TicketTest {

  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  private UserDao dao;
  private String email;

  @Before
  public void setup() {

    this.dao = new UserDao(mongoClient, databaseName);
    this.email = "user@preferences.email";
    Document userDoc = new Document("email", email);
    userDoc.put("name", "Preferencial");
    mongoClient.getDatabase(databaseName).getCollection("users").insertOne(userDoc);
  }

  @After
  public void tearDown() {
    Document userDoc = new Document("email", email);
    mongoClient.getDatabase(databaseName).getCollection("users").deleteOne(userDoc);
  }

  @Test
  public void testUpdateSinglePreferences() {

    String expected = "FC Porto";
    String key = "favorite_club";
    HashMap<String, String> userPrefs = new HashMap<>();
    userPrefs.put(key, expected);

    Assert.assertTrue(
        "The response of the updateUserPreferences should result in true. Check your implementation of this method",
        dao.updateUserPreferences(email, userPrefs));

    User user = dao.getUser(email);

    Assert.assertEquals(
        "After an update, the user preferences should be found in the User object",
        expected,
        user.getPreferences().get(key));

    Assert.assertEquals(
        "The number of keys in the user preferences does not match the expected. Check update object",
        1,
        user.getPreferences().keySet().size());
  }

  @Test
  public void testMultiplePreferences() {
    String[] values = {"Francesinha", "Sunset in Lisbon"};
    String[] keys = {"favorite_dish", "favorite_movie"};

    HashMap<String, String> userPrefs = new HashMap<>();
    userPrefs.put(keys[0], values[0]);
    userPrefs.put(keys[1], values[1]);

    Assert.assertTrue(
        "The response of the updateUserPreferences should result in true. Check your implementation of this method",
        dao.updateUserPreferences(email, userPrefs));

    User user = dao.getUser(email);

    Assert.assertEquals(
        "The number of keys in the user preferences does not match the expected. Check update object",
        2,
        user.getPreferences().keySet().size());

    Assert.assertEquals(
        "After an update, the user preferences should be found in the User object",
        values[1],
        user.getPreferences().get(keys[1]));

    Assert.assertNotEquals(
        "Looks like the user preferences got mixed up. Check your update method",
        values[1],
        user.getPreferences().get(keys[0]));
  }

  @Test(expected = IncorrectDaoOperation.class)
  public void testNullPreferences() {
    dao.updateUserPreferences(email, null);
  }
}
