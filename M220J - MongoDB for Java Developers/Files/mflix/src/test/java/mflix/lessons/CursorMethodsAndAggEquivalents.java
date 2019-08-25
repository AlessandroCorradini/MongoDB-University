package mflix.lessons;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a
 *     href="https://docs.mongodb.com/manual/reference/operator/meta/natural/index.html">$natural</a>
 */
@SpringBootTest
public class CursorMethodsAndAggEquivalents extends AbstractLesson {

  private MongoCollection<Document> sortable;

  public CursorMethodsAndAggEquivalents() {
    super();
    sortable = testDb.getCollection("sortable");
  }

  /**
   * In this lesson, we are going to review the different cursor methods, that the find command
   * provides, as well as the alternative aggregation framework stages to similar / equivalent
   * functionality, within the context of an aggregation pipeline.
   *
   * <p>We will cover the following driver methods:
   *
   * @see com.mongodb.client.FindIterable sort, limit and skip methods.
   * @see com.mongodb.client.MongoCollection aggregate method.
   *     <p>We will also briefly review how use the driver builders like:
   * @see com.mongodb.client.model.Sorts
   * @see com.mongodb.client.model.Aggregates
   */
  @Before
  public void setUp() {
    /*
     * Before we get started, looking into our cursor methods and aggregation
     * stages, I'm going to create a 100 documents in dummy collection.
     */
    List<Document> documents = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      documents.add(new Document("i", i));
    }
    sortable.insertMany(documents);
  }

  @Test
  public void testFindSortMethod() {
    /*
     * More often than not, the results of our queries are required to be
     * sorted. Databases do a good job sorting those for us, so that we
     * do not have to implement extra logic to sort the results after they've
     * been sent back to the application.
     * In MongoDB, we can do this by appending the sort() method to the
     * FindIterable.
     */

    // In this case we want to sort all my documents by field `i` in descending
    // order. We can start by defining my sort Bson, using the Sorts builder
    Bson sortBy_i_Descending = Sorts.descending("i");

    // Then I'll do a full table scan, using the find() command, and
    // appending the sort method, passing the Bson object that determines
    // my sort order.
    Iterable<Document> sorted = sortable.find().sort(sortBy_i_Descending);

    // Iterating over the result set, we will find the expected set of
    // documents
    List<Document> sortedArray = new ArrayList<>();

    sorted.iterator().forEachRemaining(sortedArray::add);

    // The number of elements should match the total 1000 documents inserted
    Assert.assertEquals(1000, sortedArray.size());

    // And the the document of the array should have an `i` value of 999
    Assert.assertEquals(Integer.valueOf(999), sortedArray.get(0).getInteger("i"));
  }

  @Test
  public void testFindSortAndSkip() {
    /*
     * In addition to sorting the query output, we might be interested in sections of the
     * result set, instead of retrieving the whole set at all times.
     * Skip is another cursor method that we can call from the Iterable
     * object.
     */

    // Out of our 1000 documents, we might want to skip the first 990.
    // For that, we just need to append the number of documents we want to
    // skip.
    Iterable<Document> skippedIterable = sortable.find().skip(990);

    List<Document> skippedArray = new ArrayList<>();
    skippedIterable.iterator().forEachRemaining(skippedArray::add);

    // The size should reflect the 10 outstanding documents (1000-990)
    Assert.assertEquals(10, skippedArray.size());

    // And the first element, should be set with the `i` value of 990
    int firstSkipped_i_value = skippedArray.get(0).getInteger("i");
    Assert.assertEquals(990, firstSkipped_i_value);

    /*
     * In this specific case, we are not specifying a particular sort
     * order to our documents. This means that the results will be returned in
     * their $natural sort order, which implies that the documents will be
     * returned based on how they got stored/created in the database server.
     * Generally this will reflect the insert order of documents.
     */

    // we can see the effect of this by removing the document with the `i`
    // value of 10.
    sortable.deleteOne(new Document("i", 10));
    // and inserting it back again.
    sortable.insertOne(new Document("i", 10));

    // By re-running the query we get the following results
    Iterable<Document> iterableAfterInsert = sortable.find().skip(999);
    for (Document d : iterableAfterInsert) {
      // there should be only one document with the `i` value of 10.
      Assert.assertEquals(Integer.valueOf(10), d.getInteger("i"));
    }

    // This means that using skip on it's own, without determining a
    // particular sort order, may result in different documents, given that
    // they are bound to the insertion order of documents, not the values
    // of the fields they represent. But there is nothing stopping us from
    // sorting them and skipping at the same time!
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> sortedAndSkipped = sortable.find().sort(sortBy_i_Descending).skip(990);

    // The order by which we define the sort() and skip() instructions is
    // irrelevant.
    Iterable<Document> skippedAndSorted = sortable.find().skip(990).sort(sortBy_i_Descending);

    // let's build a sortedFirst list, iterating over all the results of
    // sortedAndSkipped iterable
    List<Document> sortedFirst = new ArrayList<>();
    sortedAndSkipped.iterator().forEachRemaining(sortedFirst::add);

    // and a similar one for skippedFirst
    List<Document> skippedFirst = new ArrayList<>();
    skippedAndSorted.iterator().forEachRemaining(skippedFirst::add);

    // the size of the lists match
    Assert.assertEquals(skippedFirst.size(), sortedFirst.size());

    // so do all the elements in those lists.
    for (int i = 0; i < 10; i++) {
      Assert.assertEquals(skippedFirst.get(i).getInteger("i"), sortedFirst.get(i).getInteger("i"));
    }

    /*
     * Note: An important thing to understand about `skip` is that the
     * database will still have to iterate over all the documents in the
     * collection. Only returns after the skip number of documents has been
     * reached, however it will still to traverse the collection / index to
     * return the matching documents.
     */

  }

  @Test
  public void testLimitAndBatchSize() {

    /*
     * Another cursor method is limit(). The ability to define how many
     * documents we want to retrieve from the collection, on a given query.
     */

    // To impose a limit to the number of results returned by a query we
    // can append the `limit` method to the returned iterable of a find
    // instruction.
    Iterable<Document> limited = sortable.find().limit(10);
    List<Document> limitedList = new ArrayList<>();
    limited.forEach(limitedList::add);

    Assert.assertEquals(10, limitedList.size());

    /*
     * One interesting aspect of limit() is that we can use that to
     * influence the cursor batchSize. @see <a href="https://docs.mongodb.com/manual/reference/method/cursor.batchSize/">batchSize</a>
     * The cursor batch size determines the number of documents to be
     * returned in one cursor batch. If our query hits 1M elements you may
     * not want to wait till all of those elements are returned to start
     * processing the result set. Therefore each time we open a cursor or
     * iterable, the results are sent back to the application in smaller
     * batches, hence the cursor batchSize.
     */

    // By default the Java driver will set a batchSize of 0. Which means the
    // driver will use the server defined batchSize, which by default is 101
    // documents. However, you can define your own batchSize for a find
    // operation.

    sortable.find().batchSize(10);

    // In this particular case, given that we are interested in a way
    // smaller number of documents, we have all the interest to limit the
    // batch size to the same value as the limit() cursor method value
    Iterable<Document> limitedBatched = sortable.find().limit(10).batchSize(10);

    /*
     * This would be similar to running this mongo shell command:
     * <p>
     *     db.sortable.find().limit(10).batchSize(10)
     * </p>
     */

    int count = 0;
    for (Document d : limitedBatched) {
      count++;
    }

    Assert.assertEquals(10, count);
  }

  @Test
  public void testSortSkipLimit() {
    /*
     * Getting all the cursor methods together is done by appending each of
     * those methods to the resulting find iterable
     */
    Bson sortBy_i_Descending = Sorts.descending("i");
    Iterable<Document> cursor = sortable.find().sort(sortBy_i_Descending).skip(100).limit(10);
    int iValue = 899;

    for (Document d : cursor) {
      Assert.assertEquals(Integer.valueOf(iValue), d.getInteger("i"));
      iValue--;
    }
  }

  /**
   * Instead of cursor methods, we can use the aggregation stages to accomplish the same result. Why
   * do we need an aggregation stage to do this? Well, the need for intermediary sort, limit and
   * skip stages, exist in multiple pipeline executions, so these stages are readily available for
   * usage within the aggregation pipelines.
   */
  @Test
  public void testFindLimitAndAggLimitStage() {

    // Let's start with limit.
    // Using our movies dataset, we can run the following query:
    // db.movies.find({directors: "Sam Raimi"}).limit(2)
    Bson qFilter = Filters.eq("directors", "Sam Raimi");
    Iterable limitCursor = moviesCollection.find(qFilter).limit(2);

    List<Document> limitedFindList = new ArrayList<>();
    ((FindIterable) limitCursor).into(limitedFindList);
    // the size of this list should be of 2
    Assert.assertEquals(2, limitedFindList.size());

    // Now let's go ahead and do the same using $limit stage
    List<Bson> pipeline = new ArrayList<>();

    // first we need to $match the wanted director
    pipeline.add(Aggregates.match(qFilter));

    // then we limit the results to 2 using $limit
    pipeline.add(Aggregates.limit(2));

    // and then call the aggregation method
    AggregateIterable<Document> aggLimitCursor = moviesCollection.aggregate(pipeline);

    // the results of this cursor match the previously returned documents from limit().
    for (Document d : aggLimitCursor) {
      Assert.assertNotEquals(-1, limitedFindList.indexOf(d));
    }
  }

  @Test
  public void testFindSortandAggSortStage() {

    /*
     * Now let's look at $sort stage and sort() method.
     * For that we are going to run the following queries:
     * <p>
     *     db.movies.aggregate([
     *          {$match: { "directors": "Sam Raimi" }},
     *          {$sort: { "year": 1 }} //Ascending order
     *     ])
     * </p>
     *
     * And the find command:
     * <p>
     *     db.movies.find({directors: "Sam Raimi"}).sort({"year": 1})
     * </p>
     */

    // To specify the $sort stage, we can use the
    // Aggregates builder and the Sorts builder.
    Bson sortStage = Aggregates.sort(Sorts.ascending("year"));
    Bson matchStage = Aggregates.match(Filters.eq("directors", "Sam Raimi"));

    // let's now assemble the pipeline, starting with the $match stage.
    List<Bson> pipeline = new ArrayList<>();
    pipeline.add(matchStage);
    pipeline.add(sortStage);
    // and add the results to a list.
    List<Document> aggSortList = new ArrayList<>();
    moviesCollection.aggregate(pipeline).into(aggSortList);

    // Let's then do the same using the cursor method
    List<Document> findSortList = new ArrayList<>();
    moviesCollection
        .find(Filters.eq("directors", "Sam Raimi"))
        .sort(Sorts.ascending("year"))
        .into(findSortList);

    // Now both lists should have the same documents in the same order
    Assert.assertEquals(findSortList.size(), aggSortList.size());
    for (int j = 0; j < findSortList.size(); j++) {
      Assert.assertEquals(findSortList.get(j), aggSortList.get(j));
    }
  }

  @Test
  public void testSkipInAggAndFind() {

    /*
     * Let's now do the same for skip.
     * <p>
     *     db.movies.aggregate([
     *          {$match: {"directors": "Sam Raimi"}},
     *          {$skip: {count: 10}}
     *     ])
     * </p>
     *
     * <p>
     *     db.movies.find({directors: "Sam Raimi"}).skip(10)
     * </p>
     */
    // we start with the common query filter:
    Bson queryFilter = Filters.eq("directors", "Sam Raimi");
    // let's skip 990 documents
    // using aggregation
    Bson matchStage = Aggregates.match(queryFilter);
    // let's create the skip stage
    Bson skipStage = Aggregates.skip(10);
    // and create the pipeline
    List<Bson> pipeline = new ArrayList<>();
    pipeline.add(matchStage);
    pipeline.add(skipStage);
    // send all of the results into a list
    List<Document> skipAggList = new ArrayList<>();
    moviesCollection.aggregate(pipeline).into(skipAggList);

    // and now match these documents with the incoming documents from find
    int index = 0;
    for (Document d : moviesCollection.find(queryFilter).skip(10)) {
      Assert.assertEquals(skipAggList.get(index), d);
      index++;
    }
  }

  @Test
  public void testPuttingItAllTogether() {
    Bson queryFilter = Filters.eq("directors", "Sam Raimi");
    /*
     * Now that we know we can use sort() limit() and skip() methods
     * together, by appending these methods to the find() command
     */

    FindIterable<Document> findIterable =
        moviesCollection.find(queryFilter).sort(Sorts.ascending("year")).skip(10).limit(2);

    List<Document> findList = new ArrayList<>();
    findIterable.into(findList);

    // Then let's create our different aggregation stages
    Bson matchStage = Aggregates.match(queryFilter);
    Bson skipStage = Aggregates.skip(10);
    Bson sortStage = Aggregates.sort(Sorts.ascending("year"));
    Bson limitStage = Aggregates.limit(2);
    /*
     * For aggregation, the order of stages in the pipeline does matter.
     * Since we are setting up a pipeline the results will vary given the
     * processing order of the different stages;
     */

    // Let's execute the pipeline having $limit first
    List<Bson> limitFirstPipeline = new ArrayList<>();
    limitFirstPipeline.add(limitStage);
    limitFirstPipeline.add(sortStage);
    limitFirstPipeline.add(matchStage);
    limitFirstPipeline.add(skipStage);

    List<Document> limitFirstList = new ArrayList<>();
    moviesCollection.aggregate(limitFirstPipeline).into(limitFirstList);

    // This pipeline will return a completely different set of results than
    // the previous find command, because the first stage of the pipeline
    // will determine the following stages result set.

    Assert.assertNotEquals(limitFirstList.size(), findList.size());

    // By running the proper order we will get the same list of results.
    List<Bson> correctPipeline = new ArrayList<>();
    // we start with the $match stage
    correctPipeline.add(matchStage);
    // then we sort the results with $sort
    correctPipeline.add(sortStage);
    // followed by $skip
    correctPipeline.add(skipStage);
    // and ending with $limit
    correctPipeline.add(limitStage);

    List<Document> aggregationList = new ArrayList<>();
    // after running the aggregation pipeline
    moviesCollection.aggregate(correctPipeline).into(aggregationList);

    // we get the same result set in both mechanims

    Assert.assertEquals(findList, aggregationList);
  }

  /**
   * To recap: - Cursor methods have their equivalents in aggregation framework stages - The order
   * by which cursor methods are applied does not impact the result set - The order by which
   * aggregation stages are apply does!
   */
  @After
  public void tearDown() {
    // Let's also not forget to clean our dummy collection at the end of
    // each test.
    sortable.drop();
  }
}
