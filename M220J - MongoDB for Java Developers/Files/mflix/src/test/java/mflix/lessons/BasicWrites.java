package mflix.lessons;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** @see com.mongodb.MongoWriteException */
@SpringBootTest
public class BasicWrites extends AbstractLesson {

  /**
   * In this lesson we are going to look into writing new data into a collection. Specifically, we
   * are going to review the insert, insertMany and update with {upsert: true} options in the Java
   * driver.
   */
  private MongoCollection<Document> videoGames;

  @Before
  public void setUp() {
    /*
     * We start by instantiating a collection, called videoGames, in the
     * test database.
     */

    videoGames = testDb.getCollection("video_games");
  }

  @Test
  public void testWriteOneDocument() {

    /*
     * Once we have a collection to write to let's create a Document object
     * instance that will hold some information. In this case, we will
     * have a document that represents a video game.
     */

    // we set the first key, right in the constructor.
    Document doc = new Document("title", "Fortnite");

    // then we add a new set of document fields like year of the game launch
    // by appending a new key value pair
    doc.append("year", 2018);

    // and another one using put()
    doc.put("label", "Epic Games");

    // then we can insert this document by calling the collection insertOne
    // method
    videoGames.insertOne(doc);

    /*
     * As you probably noticed, the insertOne method returns void. So
     * how do we know if the document has been correctly inserted?
     * If an error occurred, during the insert of a given document, a
     * MongoWriteException would be thrown stating the origin/cause of
     * the error. In later lessons, we will look into which kind of
     * exceptions we should be prepared to handled and what causes such
     * exceptions.
     * Otherwise, everything get's correctly written and the document is
     * stored in the database.
     * However, the avid MongoDB expert in you is wondering, where can we
     * see the _id field? We did not set one, so surely it would still
     * need to be set. And you are right.
     */

    Assert.assertNotNull(doc.getObjectId("_id"));
    // On insert, the driver will set a _id field if it is not set by the
    // application, with the default datatype of ObjectId
    // https://docs.mongodb.com/manual/reference/method/ObjectId/index.html

    // This basically means that if we try to recover the document back from
    // the database, using this _id value, we will get the same document
    Document retrieved = videoGames.find(Filters.eq("_id", doc.getObjectId("_id"))).first();

    // Which we can assert that it is true
    Assert.assertEquals(retrieved, doc);
  }

  @Test
  public void testInsertMany() {
    /*
     * Another option to insert new data into a collection, is to insert it
     * in bulk, or better saying several documents at once.
     */

    // In this case I'm going to set up a list of two documents

    List<Document> someGames = new ArrayList<>();

    Document doc1 = new Document("title", "Hitman 2");
    doc1.put("year", 2018);
    doc1.put("label", "Square Enix");

    Document doc2 = new Document();
    HashMap<String, Object> documentValues = new HashMap<>();
    documentValues.put("title", "Tom Raider");
    documentValues.put("label", "Eidos");
    documentValues.put("year", 2013);
    doc2.putAll(documentValues);

    // Once we have the two documents we can add them to the list of
    // documents
    someGames.add(doc1);
    someGames.add(doc2);

    // and finally insert all of these documents using insertMany
    videoGames.insertMany(someGames);

    /*
     * The same logic applies as in insertOne, if an error occurs that
     * prevents the documents to be inserted, we would need to capture a
     * Runtime exception.
     */

    // If we look back into the object references we can see that the _id
    // fields are correctly set
    Assert.assertNotNull(doc1.getObjectId("_id"));
    Assert.assertNotNull(doc2.getObjectId("_id"));

    List<ObjectId> ids = new ArrayList<>();
    ids.add(doc1.getObjectId("_id"));
    ids.add(doc2.getObjectId("_id"));

    // And that we can retrieve them back.
    Assert.assertEquals(2, videoGames.countDocuments(Filters.in("_id", ids)));
  }

  @Test
  public void testUpsertDocument() {
    /*
     * There are times where we are not sure if a document already exists
     * in the collection, and we just want to update it if it already
     * exists. Something like with "update" or "insert" if it does not
     * exist. Well, MongoDB allows for that in a very straightforward way.
     */

    // Let's go ahead and instantiate our document
    Document doc1 = new Document("title", "Final Fantasy");
    doc1.put("year", 2003);
    doc1.put("label", "Square Enix");

    // and instead of going to the database, run a query to check if the
    // document already exists, we are going to emit an update command
    // with the flag $upsert: true.

    // We set a query predicate that finds the video game based on his title
    Bson query = new Document("title", "Final Fantasy");

    // And we try to updated. If we do not provide the upsert flag
    UpdateResult resultNoUpsert = videoGames.updateOne(query, new Document("$set", doc1));

    // if the document does not exist, the number of matched documents
    Assert.assertEquals(0, resultNoUpsert.getMatchedCount());
    // should be 0, so as the number of modified documents.
    Assert.assertNotEquals(1, resultNoUpsert.getModifiedCount());

    // on the other hand, if we do provide an upsert flag by setting the
    // UpdateOptions document
    UpdateOptions options = new UpdateOptions();
    options.upsert(true);

    // and adding those options to the update method.
    UpdateResult resultWithUpsert =
        videoGames.updateOne(query, new Document("$set", doc1), options);

    // in this case both our number of modified count
    Assert.assertEquals(0, resultWithUpsert.getModifiedCount());
    // should be still 0 given that there was no document in the
    // collection, however we do have a upsertId as result of the insert
    Assert.assertNotNull(resultWithUpsert.getUpsertedId());
    // and should be of ObjectId type
    Assert.assertTrue(resultWithUpsert.getUpsertedId().isObjectId());

    // Another component of the update or insert, upsert, is that we can
    // set values just in case we are inserting.
    // Let's say we want add a field called, just_inserted, if the
    // document did not existed before, but do not set it if the document
    // already existed

    // let's try to update "Final Fantasy", which already exists:
    // we need to setup an object that defines the update operation,
    // set's the title and appends the field just_inserted with string "yes"
    Bson updateObj1 =
        Updates.combine(
            Updates.set("title", "Final Fantasy 1"), Updates.setOnInsert("just_inserted", "yes"));

    query = Filters.eq("title", "Final Fantasy");

    UpdateResult updateAlreadyExisting = videoGames.updateOne(query, updateObj1, options);

    // In this case, the field will not be present when we query for this
    // document
    Document finalFantasyRetrieved =
        videoGames.find(Filters.eq("title", "Final Fantasy 1")).first();
    Assert.assertFalse(finalFantasyRetrieved.keySet().contains("just_inserted"));

    // on the other hand, if the document is not updated, but inserted
    Document doc2 = new Document("title", "CS:GO");
    doc2.put("year", 2018);
    doc2.put("label", "Source");

    Document updateObj2 = new Document();
    updateObj2.put("$set", doc2);
    updateObj2.put("$setOnInsert", new Document("just_inserted", "yes"));

    UpdateResult newDocumentUpsertResult =
        videoGames.updateOne(Filters.eq("title", "CS:GO"), updateObj2, options);

    // Then, we will see the field correctly set, querying the collection
    // using the upsertId field in the update result object
    Bson queryInsertedDocument = new Document("_id", newDocumentUpsertResult.getUpsertedId());

    Document csgoDocument = videoGames.find(queryInsertedDocument).first();

    Assert.assertEquals("yes", csgoDocument.get("just_inserted"));
  }

  /**
   * Let's recap: - we can insert new documents using both the insertOne or insertMany collection
   * methods - update using the $upsert flag set to true also allows us to insert new documents -
   * using $setOnInsert update operator provides a way to set specific fields only in the case of
   * insert.
   */
  @After
  public void tearDown() {
    this.videoGames.drop();
  }
}
