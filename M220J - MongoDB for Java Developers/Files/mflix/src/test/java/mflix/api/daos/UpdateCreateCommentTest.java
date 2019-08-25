package mflix.api.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mflix.api.models.Comment;
import mflix.config.MongoDBConfiguration;
import org.bson.Document;
import org.bson.types.ObjectId;
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

import java.util.Date;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UpdateCreateCommentTest extends TicketTest {

  private CommentDao dao;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  private String notValidEmail;
  private String validEmail;

  private String fakeCommentId;

  @Before
  public void setUp() {
    this.dao = new CommentDao(mongoClient, databaseName);

    this.notValidEmail = "hello@notvalid.io";
    this.validEmail = "hello@valid.io";
    this.fakeCommentId = this.dao.generateObjectId().toHexString();
    removeFakeComment(this.fakeCommentId);
  }

  @After
  public void tearDown() {
    removeFakeComment(this.fakeCommentId);
  }

  private void removeFakeComment(String id) {
    commentsCollection().deleteOne(Filters.eq("_id", id));
  }

  private Comment fakeCommentWithId() {
    Comment comment = fakeCommentNoId();
    comment.setId(this.fakeCommentId);
    return comment;
  }

  private Comment fakeCommentNoId() {
    String movieId = "573a1394f29313caabce0899";
    Comment comment = new Comment();
    comment.setEmail(validEmail);
    comment.setText(randomText(32));
    comment.setDate(new Date());
    comment.setName("some name");
    comment.setMovieId(movieId);
    return comment;
  }

  private MongoCollection commentsCollection() {
    return this.mongoClient.getDatabase(databaseName).getCollection(CommentDao.COMMENT_COLLECTION);
  }

  @Test
  public void testUserUpdatesOwnComments() {
    Comment fakeComment = fakeCommentWithId();
    dao.addComment(fakeComment);
    String expectedCommentText = randomText(20);

    Assert.assertTrue(
        "Should be able to update his own comments. Check updateComment implementation",
        dao.updateComment(fakeComment.getId(), expectedCommentText, validEmail));

    Document actualComment =
        (Document)
            commentsCollection()
                .find(new Document("_id", new ObjectId(fakeCommentId)))
                .first();

    Assert.assertEquals(
        "Comment text should match. Check updateComment implementation",
        expectedCommentText,
        actualComment.getString("text"));

    Assert.assertEquals("Commenter email should match the user email",
            validEmail, actualComment.getString("email"));
  }

  @Test
  public void testUserFailsUpdateOthersComments() {

    Comment fakeComment = fakeCommentWithId();
    dao.addComment(fakeComment);
    String newCommentText = randomText(20);

    Assert.assertTrue(
        "Cannot update comments not owned by user",
        !dao.updateComment(fakeComment.getId(), newCommentText, notValidEmail));
  }

  @Test(expected = IncorrectDaoOperation.class)
  public void testUserAddCommentWithNoID() {
    Comment actual = fakeCommentNoId();
    dao.addComment(actual);
  }

  @Test
  public void testAddCommentWithId() {
    Comment expectedComment = fakeCommentWithId();
    Assert.assertNotNull(
        "Comment should have been correctly added. Check your addComments method",
        dao.addComment(expectedComment));

    Document actualComment =
        (Document) commentsCollection().find(Filters.eq("_id", expectedComment.getOid())).first();

    Assert.assertNotNull("Comment should be found. Check your addComment method", actualComment);

    Assert.assertEquals(
        "Comment email not matching. Check your addComment method",
        actualComment.getString("email"),
        expectedComment.getEmail());

    Assert.assertEquals(
        "Comment text not matching. Check your addComment method",
        actualComment.getString("text"),
        expectedComment.getText());

    Assert.assertEquals(
        "Comment date not matching. Check your addComment method",
        actualComment.getDate("date"),
        expectedComment.getDate());
  }

  @Test
  public void testAddCommentUsingObjectId() {
    String id = "5a9427648b0beebeb69579cc";
    Comment comment = dao.getComment(id);

    Assert.assertNotNull(comment);
  }
}
