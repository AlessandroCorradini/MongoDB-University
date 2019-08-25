package mflix.lessons;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BasicDeletes extends AbstractLesson {

  /**
   * In this lesson we are going to check out how to perform deletes and remove documents that match
   * a query predicate.
   */
  MongoCollection<Document> sports;

  @Before
  public void setUp() {
    /*
     * For the purpose of this lesson, I'm going to go ahead and create a
     * sample collection, in my test database, called sports.
     */

    sports = testDb.getCollection("sports");

    // Populating the sports collection with a few examples of sports.
    List<Document> sportsList = new ArrayList<>();
    sportsList.add(new Document("name", "football").append("note", "the " + "world version"));
    sportsList.add(new Document("name", "basketball"));
    sportsList.add(new Document("name", "baseball"));
    sportsList.add(new Document("name", "table tennis"));
    sportsList.add(new Document("name", "tennis"));
    sportsList.add(new Document("name", "F1").append("note", "Motor " + "sport"));
    sportsList.add(new Document("name", "curling"));
    sportsList.add(new Document("name", "american football").append("note", "similar to rugby"));

    sports.insertMany(sportsList);
  }

  @Test
  public void testDeleteOne() {
    /*
     * The first thing to understand is that a delete operation is in fact
     * a write in database world. Confused? What I mean by this is that
     * when we delete a document from our database we are actually
     * executing a state change in the data that implies a database write
     * . Deletes imply that several different things will happen:
     * - collection data will be changed
     * - indexes will be updated
     * - entries in the oplog (replication mechanism) will be added.
     *
     * All the same operations that an insert or an update would originate.
     *
     * But let's go ahead and see this in action with a single document
     * delete.
     */

    // Before I start deleting data from my collection I'm going to count
    // the number of documents in the sports collection.
    long nSportsBefore = sports.countDocuments();

    // now that I know how many sport documents my collection holds
    // I'm going to just delete one.

    // First I need to define a query predicate (or query filter)
    Bson emptyQuery = new Document();
    // that in this case is empty. Which would be similar to running this
    // command in the mongo shell:
    // db.sports.deleteOne()
    DeleteResult dResult = sports.deleteOne(emptyQuery);

    // We can verify that the delete was successful by checking the
    // number of deleted documents in our DeleteResult, resulting object.

    Assert.assertEquals(1, dResult.getDeletedCount());

    // If I now count the number of documents I can see that it has
    // decreased by one
    Assert.assertEquals(nSportsBefore - 1, sports.countDocuments());
    // All great as expected!

    // Wait, but which document did I just remove from the collection?
    // Well, in this case MongoDB will select the first $natural document
    // it encounters in the collection and delete that one.
    // Given that the insertMany inserts documents in order, the document
    // that I have just deleted will be the one with name = football :(
    Bson queryFootball = Filters.eq("name", "football");
    Assert.assertNull(sports.find(queryFootball).iterator().tryNext());
  }

  @Test
  public void testDeleteWithPredicate() {
    /*
     * A delete that takes the first element of the collection $natural
     * order tends to
     * be a less than usual thing that you would do in your application, so
     * let's use something a bit more elaborate.
     * Let's say I would like to delete a document that matches a
     * particular name.
     */

    // let's delete the first sport where the name field starts with string
    // "bas". To do that I can express a simple regex pattern in string
    // format and call the Filters.regex method. The query in the mongo
    // shell should be similar to this:
    // db.sports.delete({"name": /^bas/})
    Bson query = Filters.regex("name", "^bas");

    DeleteResult dResult = sports.deleteOne(query);
    // after running the delete method and capturing the DeleteResult
    // we can confirm that one sport was deleted.
    Assert.assertEquals(1, dResult.getDeletedCount());

    // Now, which one did I just delete?
    // Again, the same logic will be applied, the first element that can
    // be found in this collection that matches the query predicate will
    // be the one to get deleted.
    // That means that our dear basketball sport no longer resides in the
    // collection after this delete.
    Assert.assertNull(sports.find(new Document("name", "basketball")).iterator().tryNext());

    /*
     * This actually raises an important aspect about deleting one
     * element of a multi document match query. To run these operations
     * safely, this means deleting the exact document we are looking for,
     * the match should be done using the primary key of the document.
     * Otherwise you will be deleting documents based on their initial
     * insert/update order, and that can vary widely.
     */
  }

  @Test
  public void testDeleteMany() {
    /*
     * Obviously, deleting one single document at a time when we want to
     * delete all documents that match a query predicate would be very
     * slow, therefore MongoDB also allows to delete all documents that
     * match a given criteria.
     *
     * This time around I would like to delete all documents that have a
     * particular field. Not interested in what kind of value they are
     * represented with, but more with deleting all documents that have such
     * field. If the field exists, the document has to go!
     *
     * db.sports.deleteMany({ "note": { "$exists": 1} })
     *
     */

    // Let's create the delete criteria where if the field note exists,
    // we want to delete all those documents. Using the Filters builder
    // that would be done like this
    Bson criteria = Filters.exists("note");

    // Running the deleteMany method with this criteria
    DeleteResult dManyResult = sports.deleteMany(criteria);

    // We can assert that we delete three documents in one go.
    Assert.assertEquals(3, dManyResult.getDeletedCount());
  }

  @Test
  public void testFindAndDelete() {
    /*
     * One last method that allows us to remove documents from our
     * collection is our findAndDelete method. Comparing this with the
     * deleteOne method, the difference will reside in
     * what object is returned by the method.
     * In the case of deleteOne, we get back the @see com.mongo.client.DeleteResult
     * object, that tells us if the command was acknowledge and the
     * number of documents deleted. In the case of findAndDeleteOne, what
     * we get back is the object that was just deleted. This method might
     * be very useful in use cases where we need to return back the last
     * state of a document for post purge processing by the application.
     */

    // If we try to delete the sport with name "table tennis"
    Bson query = new Document("name", "table tennis");
    Document deletedSport = sports.findOneAndDelete(query);

    // We can assert that the deletedSport is not null
    Assert.assertNotNull(deletedSport);

    // And that the document contains an _id field
    Assert.assertNotNull(deletedSport.getObjectId("_id"));
  }

  /**
   * Let's recap: - Deleting data is just another write operation in the database - We can delete
   * one document at a time with deleteOne - Or we can deleteMany documents in one go - The method
   * findAndDeleteOne returns the state, the document before the delete command got executed.
   */
  @After
  public void tearDown() {
    sports.drop();
  }
}
