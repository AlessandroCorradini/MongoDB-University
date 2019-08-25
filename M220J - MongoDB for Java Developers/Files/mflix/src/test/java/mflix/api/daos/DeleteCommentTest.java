package mflix.api.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import mflix.config.MongoDBConfiguration;
import org.bson.Document;
import org.bson.conversions.Bson;
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
public class DeleteCommentTest extends TicketTest {

  private CommentDao dao;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  private String commentId;

  private String ownerEmail = "owner@email.com";

  @Before
  public void setUp() {

    this.dao = new CommentDao(mongoClient, databaseName);

    Document commentDoc = new Document("email", ownerEmail);
    commentDoc.append("date", new Date());
    commentDoc.append("text", "some text");
    commentDoc.append("name", "user name");

    this.mongoClient.getDatabase(databaseName).getCollection("comments").insertOne(commentDoc);

    commentId = commentDoc.getObjectId("_id").toHexString();
  }

  @Test
  public void testDeleteOfOwnedComment() {

    Assert.assertTrue(
        "Should be able to delete owns comments: Check your deleteComment() method",
        dao.deleteComment(commentId, ownerEmail));
  }

  @Test
  public void testDeleteNotOwnedComment() {
    Assert.assertFalse(
        "Should not be able to delete not matching owner comment: Check your delete filter on deleteComment() method",
        dao.deleteComment(commentId, "some@email.com"));
  }

  @Test
  public void testDeleteNonExistingComment() {
    String nonExistingCommentId = new ObjectId().toHexString();
    Assert.assertFalse(
        "Deleting non-existing comment should return " + "false: Check your deleteComment() method",
        dao.deleteComment(nonExistingCommentId, ""));
  }

  @Test
  public void testDeleteIncorrectCommentId(){
    String nonExistingCommentId = new ObjectId().toHexString();
    Assert.assertFalse(
            "Deleting comment where _id value does not exist should not return true",
            dao.deleteComment(nonExistingCommentId, ownerEmail));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDeleteInvalidCommentId() {
    dao.deleteComment("", ownerEmail);
  }

  @After
  public void tearDown() {
    Bson deleteFiler = Filters.eq("_id", new ObjectId(this.commentId));
    this.mongoClient.getDatabase(databaseName).getCollection("comments").deleteOne(deleteFiler);
  }
}
