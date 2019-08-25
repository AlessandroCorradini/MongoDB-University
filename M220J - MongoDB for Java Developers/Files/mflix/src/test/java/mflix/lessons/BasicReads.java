package mflix.lessons;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
public class BasicReads extends AbstractLesson {
  public BasicReads() {
    super();
  }

  /*
   * In this lesson, we're going to perform our first read operation in
   * MongoDB using the Java driver! We've handled setting up our database
   * handle in the AbstractLesson class, so let's get started.
   */

  /**
   * If you've used other drivers in the past, you may be familiar with a findOne collection method.
   * The Java Driver for MongoDB includes no such method.
   *
   * <p>To find a single document, we'll use the limit method, which you'll learn about in a later
   * lesson. The following will find the first Document that matches the query predicate in natural
   * sort order, most likely as they were entered into the database.
   */
  @Test
  public void testCanFindOne() {
    // We do not expect the document returned to be null, so we name this
    // unexpected
    Document unexpected = null;

    // find() returns a FindIterable. We use the iterator() method to
    // convert this into a MongoCursor.
    // Because we're passing in a new Document with no information, we would
    // expect it to find the first document encountered in the collection.
    // in the mongo shell, this would look like db.movies.find().limit(1)
    MongoCursor cursor = moviesCollection.find(new Document()).limit(1).iterator();

    // use the next() method to get the next item in the iterator.
    Document actual = (Document) cursor.next();

    // Running this testDb, we should expect some random document, and after
    // we've consumed the iterator with next()
    Assert.assertNotEquals("should not be null", unexpected, actual);

    // we expect the iterator to have nothing left.
    Assert.assertFalse("the iterator should have no next", cursor.hasNext());
  }

  /**
   * Let's go ahead and find a document that matches a predicate. For this example, we'll find a
   * movie where Salma Hayek is listed in the cast field.
   */
  @Test
  public void testFindOneSalmaHayek() {
    // Let's define our query. We want a document were the value "Salma
    // Hayek" is at the cast key. Because cast is an array, MongoDB will
    // look at all elements of the Array to match this. This is because
    // MongoDB treats arrays as first-class objects.
    Document queryFilter = new Document("cast", "Salma Hayek");

    // a shorthand findOne like used in the previous testDb
    Document actual = moviesCollection.find(queryFilter).limit(1).iterator().next();

    String expectedTitle = "Roadracers";
    int expectedYear = 1994;

    Assert.assertEquals(expectedTitle, actual.getString("title"));
    Assert.assertEquals(expectedYear, (int) actual.getInteger("year"));
  }

  /**
   * What might happen if we were to call next() on an iterator that didn't have anything in it?
   *
   * <p>It will throw a NoSuchElementException
   */
  @Test(expected = NoSuchElementException.class)
  public void testNoNext() {
    // Let's issue a query that won't match anything. This will throw a
    // NoSuchElementException
    moviesCollection.find(new Document("title", "foobarbizzlebazzle")).iterator().next();
  }

  /**
   * To be safe, we should use the tryNext method. This will return null if nothing exists in the
   * iterator
   */
  @Test
  public void testTryNext() {
    Document actual =
        moviesCollection.find(new Document("title", "foobarbizzlebazzle")).iterator().tryNext();
    Assert.assertNull(actual);
  }

  /**
   * Finding one document is useful, but it's only one particular use case. What if we want to find
   * all documents that match a predicate?
   *
   * <p>One method to do so is below
   */
  @Test
  public void testFindManySalmaHayek() {
    // our query, the same as our previous "Salma Hayek" query
    Document queryFilter = new Document("cast", "Salma Hayek");

    // we'll create an ArrayList to hold our results
    List<Document> results = new ArrayList<>();

    // now we issue the query, and send them directly into our container
    moviesCollection.find(queryFilter).into(results);

    // from previously exploring the dataset, we expect 21 results
    int expectedResultsSize = 21;
    Assert.assertEquals(expectedResultsSize, results.size());

    // let's look at one of the documents now
    // we'll look at the output in the testDb results window below
    System.out.println(results.get(0).toJson());
  }

  /**
   * Looking at the document, we can see there is a lot of information. What if we only wanted the
   * title and year? You may be familiar with projection mechanics in the mongo shell, where we
   * might do something like
   *
   * <p>db.movies.find({cast: "Salma Hayek"}, { title: 1, year: 1 })
   *
   * <p>The Collection object also has projection functionality, but the usage is different than
   * that of the mongo shell.
   */
  @Test
  public void testProjection() {
    Document queryFilter = new Document("cast", "Salma Hayek");
    Document result =
        moviesCollection
            .find(queryFilter)
            .limit(1)
            .projection(new Document("title", 1).append("year", 1))
            .iterator()
            .tryNext();

    // Because we've specified that we want to keep two keys, and didn't
    // explicitly remove the _id key, we expect to get a Document back
    // with 3 keys: title, year, and _id
    Assert.assertEquals(3, result.keySet().size());

    // And let's make sure they are what we expected
    Assert.assertTrue(result.keySet().containsAll(Arrays.asList("_id", "title", "year")));
  }

  /**
   * As we saw, unless we explicitly remove the _id key it will remain in the result or results. To
   * remove the _id, we can pass 0 as the projection argument. Remember, 1 means retain, and 0 means
   * remove.
   *
   * <p>For this example, let's also search for movies where Johnny Depp is a cast member as well
   */
  @Test
  public void testProjectsAway_id() {
    // we'll build a query so that only movies with both Salma Hayek and
    // Johnny Depp are returned. For that, we'll use the $all operator
    // in the shell, this would be
    // db.movie.find({cast: { $all: ["Salma Hayek", "Johnny Depp"] }})
    Document queryFilter =
        new Document("cast", new Document("$all", Arrays.asList("Salma Hayek", "Johnny Depp")));

    List<Document> results = new ArrayList<>();
    moviesCollection
        .find(queryFilter)
        .projection(new Document("title", 1).append("year", 1).append("_id", 0))
        .into(results);

    // There should only be 1 result in our dataset
    Assert.assertEquals(1, results.size());

    // Now we should only have 2 keys, title and year
    Document firstResult = results.get(0);
    Assert.assertEquals(2, firstResult.keySet().size());
    Assert.assertTrue(firstResult.keySet().containsAll(Arrays.asList("title", "year")));

    // If this feels ugly to you, don't worry. The driver provides a much
    // nicer way to compose your queries. More in a future lesson!

  }

  /*
   * Let's Summarize:
   *
   * Querying MongoDB through the Driver may feel odd at first, but eventually
   * it will feel like second nature.
   *
   * We saw how to limit the results to one document or get all documents that
   * match a query filter. We also saw how to include only the the fields we
   * wanted in the result set, and how to remove the _id field.
   *
   * A few things to keep in mind:
   *
   * * Finding one document typically involves querying on a unique index,
   * * such as the _id field.
   *
   * * When projecting, by specifying inclusion (for example, title, 1) all
   * * fields we don't include will be excluded, except for the _id field. If
   * * we don't want the _id field returned to us we must explicitly exclude
   * * it.
   *
   * * Lastly, the specific query patterns you saw here are much easier to
   * * build. Again, more in a future lesson!
   */
}
