package mflix.lessons;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

@SpringBootTest
public class QueryBuilders extends AbstractLesson {
  public QueryBuilders() {
    super();
  }

  /**
   * In this lesson, we're going to use the Builders that are included in the MongoDB Java driver to
   * greatly ease the composition and structure of our queries.
   *
   * <p>There are several Builder classes to help us. They are:
   *
   * @see com.mongodb.client.model.Filters
   * @see com.mongodb.client.model.Projections
   * @see com.mongodb.client.model.Sorts
   * @see com.mongodb.client.model.Aggregates
   * @see com.mongodb.client.model.Updates
   * @see com.mongodb.client.model.Indexes
   *     <p>In this lesson we'll go over Filters and Projections.
   *     <p>We'll start with Filters
   */
  @Test
  public void testContrastSingleFieldQuery() {

    // Let's find a movie with Salma Hayek in it, composing a query directly
    // with a Document, and an identical query with Filters builders. In
    // the shell, this would be:
    // db.movies.find({cast: "Salma Hayek"}).limit(1)

    // First, we'll look at what the raw Document looks like
    Document onerousFilter = new Document("cast", "Salma Hayek");
    Document actual = moviesCollection.find(onerousFilter).limit(1).iterator().tryNext();

    // Given what we know from the dataset
    String expectedTitle = "Roadracers";
    int expectedYear = 1994;

    Assert.assertEquals(expectedTitle, actual.getString("title"));
    Assert.assertEquals(expectedYear, (int) actual.getInteger("year"));

    // And now with the Filters Builder API
    // Notice we've made the switch from Document to Bson
    Bson queryFilter = eq("cast", "Salma Hayek");
    Document builderActual = moviesCollection.find(queryFilter).limit(1).iterator().tryNext();

    // we should have found the same document
    Assert.assertEquals(actual, builderActual);
  }

  /**
   * I don't know about you, but this feels like a much better API to me. Let's look at a query
   * where we wanted to find movies with both Salma Hayek and Johnny Depp as cast members.
   */
  @Test
  public void contrastArrayInQuery() {
    // in the shell, this would be
    // db.movie.find({cast: { $all: ["Salma Hayek", "Johnny Depp"] }})
    Document oldFilter =
        new Document("cast", new Document("$all", Arrays.asList("Salma Hayek", "Johnny Depp")));
    List<Document> oldResults = new ArrayList<>();
    moviesCollection.find(oldFilter).into(oldResults);
    // There should only be 1 result in our dataset
    Assert.assertEquals(1, oldResults.size());

    // Now, let's look at this with the Filter Builder
    Bson queryFilter = all("cast", "Salma Hayek", "Johnny Depp");
    List<Document> results = new ArrayList<>();
    moviesCollection.find(queryFilter).into(results);

    Assert.assertEquals(results, oldResults);
  }

  /**
   * I'd argue the query is much cleaner. Filters contains all of the operators you may be used to.
   * Let's issue a query with multiple predicates.
   *
   * <p>We'll find all movies where Tom Hanks is a cast member, released after 1990 but before 2005,
   * with a minimum metacritic of 80.
   */
  @Test
  public void testMultiplePredicates() {
    // in the shell, this query would look like
    // db.movies.find({
    //     cast: "Tom Hanks",
    //     year: { $gte: 1990, $lt: 2005 },
    //     metacritic: { $gte: 80 }
    // })
    Bson queryFilter =
        and(
            // matching tom hanks
            eq("cast", "Tom Hanks"),
            // released after 1990
            gte("year", 1990),
            // but before 2005
            lt("year", 2005),
            // with a minimum metacritic of 80
            gte("metacritic", 80));
    List<Document> results = new ArrayList<>();
    moviesCollection.find(queryFilter).into(results);

    Assert.assertEquals(4, results.size());

    // 4 films. Let's enumerate their titles
    Set<String> titles =
        results.stream().map(movie -> (String) movie.get("title")).collect(Collectors.toSet());
    Assert.assertTrue(
        titles.containsAll(
            Arrays.asList("Forrest Gump", "Toy Story", "Toy Story 2", "Saving Private Ryan")));
  }

  /**
   * Filters are powerful and easier to work compared to assembling a Document. We highly recommend
   * you use them.
   *
   * <p>For more information about Filters and all that are available, see
   * http://mongodb.github.io/mongo-java-driver/3.8/builders/filters/
   *
   * <p>Let's move onto Projections
   *
   * <p>Projections are very succinct and expressive, allowing us to say what we want. Let's look at
   * an example.
   */
  @Test
  public void testProjectionBuilder() {
    // Let's now look at Projections. In the projection we'll specify
    // we want only the title and year fields, and "forget" to project
    // away the _id field.
    // In the shell, this would be:
    // db.movies.find({cast: "Salma Hayek"}, { title: 1, year: 1 })

    Document oldFilter = new Document("cast", "Salma Hayek");
    Document oldResult =
        moviesCollection
            .find(oldFilter)
            .limit(1)
            .projection(new Document("title", 1).append("year", 1))
            .iterator()
            .tryNext();

    // And we should have 3 keys, title, year and _id
    Assert.assertEquals(3, oldResult.keySet().size());
    Assert.assertTrue(oldResult.keySet().containsAll(Arrays.asList("_id", "title", "year")));

    // With Projections, this now takes on the following form
    Bson queryFilter = and(eq("cast", "Salma Hayek"));
    Document result =
        moviesCollection
            .find(queryFilter)
            .limit(1)
            // this feels much more declarative
            .projection(fields(include("title", "year")))
            .iterator()
            .tryNext();

    // We should have the same results
    Assert.assertEquals(oldResult, result);

    // And to exclude the _id field
    // db.movies.find({cast: "Salma Hayek"}, { title: 1, year: 1, _id: 0 })

    Document newResult =
        moviesCollection
            .find(queryFilter)
            .limit(1)
            // this feels much more declarative
            .projection(fields(include("title", "year"), exclude("_id")))
            .iterator()
            .tryNext();

    Assert.assertEquals(2, newResult.keySet().size());

    // If we only want to exclude the _id field, there is a convenience
    // method to do so, excludeId()

    Document no_id =
        moviesCollection
            .find(queryFilter)
            .limit(1)
            .projection(fields(include("title", "year"), excludeId()))
            .iterator()
            .tryNext();

    Assert.assertEquals(newResult, no_id);
  }

  /*
   * For more information about Projections, visit
   * http://mongodb.github.io/mongo-java-driver/3.8/builders/projections/
   * <p>
   * This concludes our lesson on Builders. To reference the other Builders
   * available and see how they are used, visit
   * http://mongodb.github.io/mongo-java-driver/3.8/builders
   * <p>
   * Let's summarize:
   * <p>
   * Builders allow us to be extremely expressive. They include all of the
   * query operators and projection operators to allow us to fine-tune our
   * queries.
   * <p>
   * Here are a few things to keep in mind:
   * <p>
   * * Filters can be composed together for robust queries
   * <p>
   * * Projections offers methods such as include(), exclude(), and
   * * excludeId()
   * * * Projections also includes methods for every other projection
   * * operation supported by MongoDB
   *
   */

}
