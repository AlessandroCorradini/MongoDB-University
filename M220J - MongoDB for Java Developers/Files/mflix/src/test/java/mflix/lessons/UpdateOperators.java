package mflix.lessons;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Updates.*;

/**
 * Hello! In this lesson we are going to talk about Update operators. Imagine a scenario where you
 * are have a database of your favorite artists and you are trying to keep up with the public
 * opinion about them, so you periodically update the rating of your favorite musicians to match
 * their public rating.
 */
public class UpdateOperators extends AbstractLesson {

  private ObjectId band1Id;
  private ObjectId band2Id;
  private Document bandOne;
  private Document bandTwo;

  public UpdateOperators() {
    super();
  }

  /* First, we must set up a test collection and the documents that go in
   * it. We are calling this collection "artists" and throwing Gorillaz and
   * Weird Al Yankovic in there for testing purposes.
   */

  @Before
  public void setUp() {
    // establish a connection
    MongoCollection<Document> musicians = testDb.getCollection("artists");

    // create a document for Gorillaz
    bandOne = new Document();
    bandOne.append("title", "Gorillazz");
    bandOne.append("num_albums", 6);
    bandOne.append("genre", "worldbeat");
    bandOne.append("rating", 8);

    // create a document for Weird Al
    bandTwo = new Document();
    bandTwo.append("title", "Weird Al Yankovic");
    bandTwo.append("num_albums", 14);
    bandTwo.append("genre", "musical parodies");
    bandTwo.append("rating", 8);

    // append the documents to a list
    List<Document> listOfMusicians = new ArrayList<>();
    listOfMusicians.add(bandOne);
    listOfMusicians.add(bandTwo);

    // insert the list in to the collection
    musicians.insertMany(listOfMusicians);

    band1Id = bandOne.getObjectId("_id");
    band2Id = bandTwo.getObjectId("_id");
  }

  /* You may have noticed that by some coincidence the name of the band
   * Gorillaz was mis-spelled with an extra z at the end. We would definitely
   * like to fix that right away and replace that value with a proper
   * spelling. There is a method that allows us to replace a Document and
   * it is called replaceOne. Here is an example of how to use it.
   */

  @Test
  public void testReplaceDocument() {
    // establish a connection with the db and retrieve the test collection
    MongoCollection<Document> artists = testDb.getCollection("artists");

    // retrieve the band in question
    Bson queryFilter = new Document("_id", band1Id);
    Document myBand = artists.find(queryFilter).iterator().tryNext();

    // make sure that this is the band that I wanted, with all the fields
    // intact
    Assert.assertEquals(
        "Make sure that the band created in the database is"
            + " the same as the band retrieved from the database",
        bandOne,
        myBand);

    // create a document with a correct band title
    Document replaceBand = new Document();
    replaceBand.append("title", "Gorillaz");

    // replace the incorrectly titled document, with the correctly
    // titled document
    artists.replaceOne(eq("_id", band1Id), replaceBand);

    // retrieve the doc after the replacement is complete
    Document myNewBand = artists.find(queryFilter).iterator().tryNext();
    // see what else changed
    Assert.assertEquals(myNewBand.get("_id"), band1Id);
    Assert.assertEquals(myNewBand.get("title"), "Gorillaz");
    Assert.assertNull(myNewBand.get("num_albums"));
    Assert.assertNull(myNewBand.get("genre"));
    Assert.assertNull(myNewBand.get("rating"));

    /* It looks like this replacement operation annihilated all fields
     * other than the _id field and the title field. This is because when
     * the replace method is used, it replaces entire Documents, and
     * since the replacement document that we created only had the title
     * and _id fields we ended up losing a bunch of data, such as the
     * number of albums, genre and rating of the band, which is unfortunate.
     *
     * Instead of using a method that replaces full documents we should
     * use something that can work with better precision and change only
     * the field that is specified. Sounds to me like we should use set.
     *
     * Let us take a look at an example that uses set when changing the
     * band title.
     *
     */

  }

  @Test
  public void testSetFieldValueForOneDocument() {
    // get the collection from the database
    MongoCollection<Document> artists = testDb.getCollection("artists");
    Bson queryFilter = new Document("_id", band1Id);
    // retrieve the band in question
    Document wrongBandName = artists.find(queryFilter).iterator().tryNext();
    // make sure that we got the right band
    Assert.assertEquals(wrongBandName.get("title"), "Gorillazz");
    // update the field that needs changes using set and updateOne
    artists.updateOne(queryFilter, set("title", "Gorillaz"));
    // retrieve the updated document
    Document updatedBand = artists.find(queryFilter).iterator().tryNext();

    // verify that the field value was updated and all other fields still
    // contain the data in other fields
    Assert.assertEquals(updatedBand.get("title"), "Gorillaz");
    Assert.assertNotNull(updatedBand.get("num_albums"));
    Assert.assertNotNull(updatedBand.get("genre"));
    Assert.assertNotNull(updatedBand.get("rating"));
  }

  /* Success! We updated the value of the title field and the rest of
   * the fields remained as they were, without being erased from existence,
   * which is great news. This is exactly what we were looking for.
   *
   * Let us look at a slightly different scenario now. Suppose Weird Al and
   * Gorillaz are now equally more popular than before and we want to
   * update their rating to the appropriate 9 from the measly 8 that is in
   * the database.
   *
   * One way of doing that is by using updateMany in combination with set.
   *
   */

  @Test
  public void testSetFieldValueForManyDocument() {
    // retrieve the collection
    MongoCollection<Document> artists = testDb.getCollection("artists");
    // use updateMany with set
    artists.updateMany(eq("rating", 8), set("rating", 9));
    // retrieve the updated documents
    Document bandOneUpdated = artists.find(new Document("_id", band1Id)).iterator().tryNext();
    Document bandTwoUpdated = artists.find(new Document("_id", band2Id)).iterator().tryNext();
    // make sure that both values are updated for both bands
    Assert.assertEquals(bandOneUpdated.get("title"), "Gorillazz");
    Assert.assertEquals(bandTwoUpdated.get("title"), "Weird Al Yankovic");
    Assert.assertEquals(bandOneUpdated.get("rating"), 9);
    Assert.assertEquals(bandTwoUpdated.get("rating"), 9);
  }

  /* Another success story! However, there is still a better way to make
   * this update happen.
   *
   * In the above example we are working with numbers. To be more specific,
   * we are working with integers, so we can use an operation that is best
   * suited for increasing integer values.
   *
   * Below is an example of how to achieve the same update result using an
   * increment operator.
   *
   *
   */

  @Test
  public void testIncFieldValue() {
    // retrieve the collection
    MongoCollection<Document> artists = testDb.getCollection("artists");
    // updateMany documents with the increment operator, increasing the
    // rating value of every document which currently has a rating value of
    // 8 by one.
    artists.updateMany(eq("rating", 8), inc("rating", 1));
    // retrieve the updated documents
    Document bandOneUpdated = artists.find(new Document("_id", band1Id)).iterator().tryNext();
    Document bandTwoUpdated = artists.find(new Document("_id", band2Id)).iterator().tryNext();

    // verify that the update was successful

    Assert.assertEquals(bandOneUpdated.get("title"), "Gorillazz");
    Assert.assertEquals(bandTwoUpdated.get("title"), "Weird Al Yankovic");
    Assert.assertEquals(bandOneUpdated.get("rating"), 9);
    Assert.assertEquals(bandTwoUpdated.get("rating"), 9);
  }

  /* Finally, we should talk about those moments when you realize that some
   * data in your documents is unnecessary. Not only is the data unnecessary,
   * but you simply don't need the fields to be present in the document at
   * all.
   *
   * Imagine that you realized that you no longer care about the public
   * opinion and you just like the music that you like regardless of what
   * the rest of the world thinks about it. So you go into your database
   * and you delete the "rating" field.
   *
   * Here is an example of how you would do that.
   *
   *
   */

  @Test
  public void testUnsetFieldValue() {
    // connect to the db and collection
    MongoCollection<Document> artists = testDb.getCollection("artists");
    // update all documents that possess the "rating" field by unsetting
    // that field. unset removes the field from the document.
    artists.updateMany(exists("rating"), unset("rating"));
    // retrieve updated documents
    Document bandOneUpdated = artists.find(new Document("_id", band1Id)).iterator().tryNext();
    Document bandTwoUpdated = artists.find(new Document("_id", band2Id)).iterator().tryNext();
    // confirm that these documents no longer contain the rating field
    // but still contain other fields.
    Assert.assertEquals(bandOneUpdated.get("title"), "Gorillazz");
    Assert.assertEquals(bandTwoUpdated.get("title"), "Weird Al Yankovic");
    Assert.assertNotNull(bandOneUpdated.get("num_albums"));
    Assert.assertNotNull(bandOneUpdated.get("genre"));
    Assert.assertNotNull(bandTwoUpdated.get("genre"));
    Assert.assertNull(bandOneUpdated.get("rating"));
    Assert.assertNull(bandTwoUpdated.get("rating"));
  }

  @After
  public void tearDown() {
    testDb.getCollection("artists").drop();
    band1Id = null;
    band2Id = null;
  }
}
    /* And we're done with update operators! Let's quickly recap what we
     * learned.
     *
     * 1. You can replace entire documents with replaceOne. This operation
     * may cause you to lose data, so it isn't recommended for situations
     * when you need a simple update.
     *
     * 2. You can update a value in a single document using the updateOne and
     * set or inc operators.
     *
     * 3. You can update multiple documents that match your query using
     * updateMany in conjunction with set or inc operations.
     *
     * 4. You can completely remove a field from a document by using
     * updateOne or updateMany with the unset operation.
     *
     * For more update operators and their use cases, feel free to check out
     * the following documentation page:
     *
     * @http://mongodb.github.io/mongo-java-driver/3.8/driver/tutorials/perform-write-operations/
     *
     * Use these tools wisely and have fun building Mflix!
     *
     */
