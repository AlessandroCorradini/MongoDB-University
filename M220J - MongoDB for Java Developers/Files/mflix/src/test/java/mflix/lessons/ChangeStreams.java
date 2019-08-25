package mflix.lessons;

import org.springframework.boot.test.context.SpringBootTest;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import mflix.lessons.utils.ChangeStreamsInsertAndUpdate;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/* Hello!
 * In this lesson we will cover Change Streams, a process that allows
 * applications to access real-time data changes without the complexity and
 * risk of tailing the oplog.
 *
 * With Change Streams you can watch all the write operations or select
 * a more precise set of changes that you are looking to track in your
 * database.
 *
 *
 */
@SpringBootTest
public class ChangeStreams extends AbstractLesson {

  // cleaning up the printed output so that only error level notifications
  // are visible
  private static final Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

  static {
    root.setLevel(Level.ERROR);
  }

  // get the comments collection, and create a Thread object
  private final MongoCollection<Document> comments = db.getCollection("comments");
  private Thread insertAndUpdateThread;

  /**
   * Before we start watching changes in our namespaces, we we are going to setup a thread that
   * writes some data into our database.
   *
   * @see ChangeStreamsInsertAndUpdate
   */
  @Before
  public void setUp() {
    ChangeStreamsInsertAndUpdate inserterAndUpdater = new ChangeStreamsInsertAndUpdate(comments);
    insertAndUpdateThread = new Thread(inserterAndUpdater);
  }

  /* First, if we want to track the write changes to the collection we
  * can use a `watch` method. This method activates Change Stream
  * response, which has the following fields in it:
  *
  * We rarely want to track all of the writes to a given
  * namespace, so in our first example we will track all the insert
  * operations to our collection.
  *
  * Change streams allow us to express filters using the aggregation
  * framework, given so, we will create an insert filter that extracts
  * the `insert` operation type from the Change Streams Document, which is
  * created per each write event that happens in the collection.
  *
  * We will populate an array of change stream documents in order to
  * verify that the changes that we are inflicting onto our collection are
  * correctly tracked in the thread.
  * Our print statement prints the documents that the change streams
  * picked up while running the insert and update process. It looks like
  * it picked up all the documents that it was supposed to, which is great
  * .
  *
  */

  @Test
  public void testWhatchAllInserts() {

    insertAndUpdateThread.start();

    List<Bson> insertFilter =
        singletonList(Aggregates.match(Filters.in("operationType", "insert")));

    List<Document> allInserts = new ArrayList<>();

    for (ChangeStreamDocument<Document> d : comments.watch(insertFilter)) {
      // print the change stream document that has an `insert`
      // operation type.
      Document insertedDoc = d.getFullDocument();
      System.out.println(insertedDoc);

      if (insertedDoc.containsKey("stop")) {
        break;
      }
      // add the change stream document to the allInserts array.
      allInserts.add(insertedDoc);
    }

    Assert.assertEquals(10, allInserts.size());
  }

  /*
  * Change Streams utilizes the Aggregation Pipeline framework, which
  * allows us to access very precise information using that framework.
  *
  *
  * In this case we want to track only the documents in which the `even`
  * field value is 1. For that, we need to create a criteria that matches
  * a few fields in the change event, so that we create a change stream
  * document only for those select events.
  *
  * Here, we add $match stage to the aggregation pipeline,
  *
  * which now says that we are interested in a document if the operation
  * type is insert AND the document inserted contains a field called even
  * with a value of one.
  *
  * Notice that in this case we are accessing the inserted document, both in
  * the aggregation pipeline and in a print statement. For that we can use
  * the getFullDocument method on a changeStreams object, which is what we
  * did in this and previous test case.
  *
  * We were expecting 5 even documents to be inserted, and our expectations
  * were met. To satisfy my own curiosity I printed out the documents that
  * were inserted using the same getFullDocument method.
  *
  */

  @Test
  public void testWatchAllEvenInserts() {
    insertAndUpdateThread.start();

    List<Bson> evenFilter =
        singletonList(
            Aggregates.match(
                Filters.and(
                    Document.parse("{'fullDocument.even': 1}"),
                    Filters.eq("operationType", "insert")))); //
    List<Document> allInserts = new ArrayList<>();

    for (ChangeStreamDocument<Document> d : comments.watch(evenFilter)) {

      Document insertedEvenDoc = d.getFullDocument();
      System.out.println(insertedEvenDoc);

      if (insertedEvenDoc.containsKey("stop")) {
        break;
      }

      allInserts.add(insertedEvenDoc);
    }
    Assert.assertEquals(5, allInserts.size());
  }

  /* This time, I want to track updates! How do we do that?
  *
  * First, change the filter from tracking insert operations to update
  * ones, and to change it up, we now want to only see the odd ones.
  *
  * Then, specify that you are doing an update_lookup with fullDocument
  * method in addition to watching. If we don't specify that we want the
  * full document, then the change event will only contain the update
  * object.
  * The question now is do we really need the whole updated document? After
  * all, we're only updating the value of one field.
  *
  */

  @Test
  public void testWatchAllUpdates() {
    insertAndUpdateThread.start();
    List<Bson> oddFilter =
        singletonList(
            Aggregates.match(
                Filters.and(
                    Document.parse("{'fullDocument.even': 0}"),
                    Filters.eq("operationType", "update"))));
    List<Document> allUpdates = new ArrayList<>();
    for (ChangeStreamDocument<Document> d :
        comments.watch(oddFilter).fullDocument(FullDocument.UPDATE_LOOKUP)) {
      Document updatedDoc = d.getFullDocument();
      System.out.println(updatedDoc);
      if (updatedDoc.containsKey("stop")) {
        break;
      }

      allUpdates.add(updatedDoc);
    }
    Assert.assertEquals(5, allUpdates.size());
  }

  /* In this test case we will print out only the information that tells us
  * about the updates that occurred in the updated document, a.k.a the
  * update object that we were avoiding in the previous example.
  *
  * To implement this, we use the getUpdateDescription in conjunction with
  * the Change Streams Document.
  *
  * Excellent! We see that no fields were removed and the `i` field was
  * updated with the value printed.
  *
  * In this case we were also watching only the odd updates, but we can
  * change that filter by modifying the aggregation pipeline that we use.
  *
  *
  */

  @Test
  public void testWatchFieldUpdates() {
    insertAndUpdateThread.start();

    List<Bson> oddFilter =
        singletonList(
            Aggregates.match(
                Filters.and(
                    Document.parse("{'fullDocument.even': 0}"),
                    Filters.eq("operationType", "update"))));

    List<Document> allUpdates = new ArrayList<>();

    for (ChangeStreamDocument<Document> d :
        comments.watch(oddFilter).fullDocument(FullDocument.UPDATE_LOOKUP)) {

      Document updatedDoc = d.getFullDocument();

      // only print the information about the updates that occurred
      System.out.println(d.getUpdateDescription());

      if (updatedDoc.containsKey("stop")) {
        break;
      }

      allUpdates.add(updatedDoc);
    }
    Assert.assertEquals(5, allUpdates.size());
  }

  /* These are some of the applications of the change streams functionality.
   *
   *
   * Use this new found knowledge responsibly and if you
   * watched the lectures in order, congratulations on being
   * done with the lessons in this course! We hope you enjoyed it.
   *
   *
   */

  @After
  public void tearDown() {
    Bson forDeletion = Filters.exists("i");
    comments.deleteMany(forDeletion);
    comments.deleteOne(Filters.exists("stop"));
  }
}
