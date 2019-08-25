package mflix.lessons;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/* Hello!
 * In this lesson we are going to talk about Error Handling. We will
 * consider a few scenarios and error categories and find ways ways to fix those
 * errors.
 *
 * This way we wan to ensure that your application is resilient to issues that
 * can occur in concurrent and distributed systems.
 *
 * Concurrent system will most likely have situations where duplicate
 * keys will occur, while distributed systems are prone to issues
 * related to network and concurrency.
 *
 * While all of the errors covered in the lesson aren't very likely to
 * occur, it is helpful to know how to deal with them if and when they manifest
 * themselves.
 */

public class ErrorHandling extends AbstractLesson {

  private MongoCollection<Document> errors;

  @Before
  public void setUp() {
    // create a test database called "errors"
    errors = testDb.getCollection("errors");
    // add one document to the "errors" database with the _id value set to 0
    errors.insertOne(new Document("_id", 0));
  }

  /* First common error can occur when you are trying to insert a document
   *  in place of an already existing document. In our example there is
   *  already a document with _id that equals 0, so inserting another
   *  document with the same _id value should cause a duplicate key error.
   *
   *  Let's test to see if this is true.
   *
   *  In this test case we are specifying that we are expecting to get a
   *  Write Exception error.
   */

  @Test(expected = MongoWriteException.class)
  public void testDuplicateKeyOne() {

    errors.insertOne(new Document("_id", 0));
  }

  /* Great! It looks like the test passed, but it would be great to know
   * exactly what kind of error we are getting. In this test case
   * you can see that the error returned is the Duplicate Key error, which
   * means that in order to correct it we should not be trying to insert a
   * Document with an existing key. Simply changing the key value should do
   * the trick.
   */

  @Test
  public void testDuplicateKeyTwo() {
    try {

      errors.insertOne(new Document("_id", 0)); // change 0 to 1 to fix
      // the error

    } catch (MongoWriteException e) {

      Assert.assertEquals(e.getError().getCategory(), ErrorCategory.DUPLICATE_KEY);
    }
  }

  /* Another error to be on the lookout for is the timeout error. In this
   * test case we are trying to avoid breaking the application by using
   * the try/catch block.
   *
   * This particular test case won't cause a timeout error. In fact, it is
   * very hard to induce a timeout error or any of the errors covered in
   * this lesson on an application that is running on Atlas.
   *
   * But if that does happen, then a try/catch block will help
   * you identify the situation.
   *
   * To fix a timeout issue you need to consider the needs of your
   * application and depending on that you can either reduce durability
   * guarantees by changing the write concern value or increase the timeout
   * and keep the app durability.
   */

  @Test
  public void testTimeoutErrorTwo() {
    try {
      errors
          .withWriteConcern(WriteConcern.MAJORITY.withWTimeout(2, TimeUnit.MICROSECONDS))
          .insertOne(new Document("key", "value"));
    } catch (MongoWriteException e) {
      System.out.println(e.getError().getCategory());
    }
  }

  /*
   *
   * Another possible error can occur when the write concern that is
   * requested cannot be fulfilled. For example, our replica set has 3
   * nodes that was automatically created by Atlas. We can dictate the type
   * of write concern that we want for our write operations. In the example
   * below we are asking for a 5 node acknowledgement, which is impossible
   * in our situation. As a result we get a Write Concern Exception. This
   * error is easy to solve by either assigning a majority write concern or
   * a number that is less than or equal to 3.
   *
   *
   */

  @Test(expected = MongoWriteConcernException.class)
  public void testWriteConcernError() {

    errors
        .withWriteConcern(WriteConcern.ACKNOWLEDGED.withW(5))
        .insertOne(new Document("key", "value"));
  }

  @After
  public void tearDown() {
    errors.drop();
  }

  // That's it for our lesson on Error handling. Enjoy the rest of the course!
}
