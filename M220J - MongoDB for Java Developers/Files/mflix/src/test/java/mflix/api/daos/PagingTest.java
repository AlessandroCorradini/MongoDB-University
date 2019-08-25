package mflix.api.daos;

import com.mongodb.client.MongoClient;
import mflix.config.MongoDBConfiguration;
import org.bson.Document;
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

import java.util.List;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PagingTest extends TicketTest {

  private MovieDao dao;
  private String sortKey;
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setup() {
    this.dao = new MovieDao(mongoClient, databaseName);
    this.sortKey = "tomatoes.viewer.numReviews";

  }

  @Test
  public void testPagingByCast() {
    String cast = "Michael Caine";
    int skip = 0;
    int countPage1 = 0;
    List<Document> movieDocs = dao.getMoviesByCast(sortKey, 20, skip, cast);
    Assert.assertEquals(
        "Expected `title` field does match: Please check your \" + \"getMoviesByCast() movies sort order.",
        "The Dark Knight",
        movieDocs.get(0).getString("title"));

    countPage1 = movieDocs.size();
    Assert.assertEquals(
        "Check the query used in getMoviesByCast() in MoviesDao.java", 20, countPage1);

    int countPage2 = 0;
    movieDocs = dao.getMoviesByCast(sortKey, 20, countPage1, cast);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your \" + \"getMoviesByCast() movies sort order.",
            "Educating Rita",
            movieDocs.get(0).getString("title"));
    countPage2 = movieDocs.size();
    Assert.assertEquals(
        "Incorrect count in page 2. Check your query implementation", 20, countPage2);

    int countPage3 = 0;
    movieDocs = dao.getMoviesByCast(sortKey, 20, countPage1 + countPage2, cast);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your \" + \"getMoviesByCast() movies sort order.",
            "World War II: When Lions Roared",
            movieDocs.get(0).getString("title"));
    countPage3 = movieDocs.size();

    Assert.assertEquals("Incorrect count in page 3", 13, countPage3);

    Assert.assertEquals(
        "Total document count does not match", 53,
            countPage1 + countPage2 + countPage3);
  }

  @Test
  public void testPagingByGenre() {
    String genre = "History";
    int skip = 0;
    int countPage1 = 0;

    List<Document> movieDocs = dao.getMoviesByGenre(sortKey, 20, skip, genre);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your \" + \"getMoviesByGenre() movies sort order.",
            "Braveheart",
            movieDocs.get(0).getString("title"));
    countPage1 = movieDocs.size();
    int expectedTotal = 999;
    Assert.assertEquals("Check the query used in () in MoviesDao.java", 20, countPage1);

    Assert.assertEquals(
        "Total document count does not match expected. Review " + "getGenreSearchCount()",
            expectedTotal,
        dao.getGenresSearchCount(genre));

    // jump to last page

    int lastPage = expectedTotal / 20;
    skip = lastPage * 20;
    int countPageFinal = 0;
    movieDocs = dao.getMoviesByGenre(sortKey, 20, skip, genre);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your " + "getMoviesByGenre() movies sort order.",
            "Only the Dead",
            movieDocs.get(0).getString("title"));
    countPageFinal = movieDocs.size();

    Assert.assertEquals(
        "Last page count does not match expected. Check dataset and getGenreSearchCount()", expectedTotal % 20,
        countPageFinal);
  }

  @Test
  public void testPagingByText() {
    String keywords = "bank robbery";
    int expectedCount = 475;
    int count = 0;
    List<Document> movieDocs = dao.getMoviesByText(20, 0, keywords);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your \" + \"getMoviesByText() movies sort order.",
            "The Bank",
            movieDocs.get(0).getString("title"));

    count = movieDocs.size();
    Assert.assertEquals("Check the query used in getMoviesByText() in MoviesDao.java", 20, count);
    Assert.assertEquals("Check your count method", expectedCount,
            dao.getTextSearchCount(keywords));

  }

  @Test
  public void testPagingByTextAndSkip(){

    String keywords = "magic";
    int limit = 20;
    int skip = 280;
    int expectedCount = 296;
    String expectedTitle = "Estomago: A Gastronomic Story";
    int finalCount = 0;
    List<Document> movieDocs = dao.getMoviesByText(limit, skip, keywords);
    Assert.assertEquals(
            "Expected `title` field does match: Please check your \" + \"getMoviesByText() movies sort order.",
            expectedTitle,
            movieDocs.get(0).getString("title"));
    finalCount = movieDocs.size();

    Assert.assertEquals("Check your getMoviesByText method.", 16, finalCount);

    Assert.assertEquals(
        "Check the query used in getMovies() in MoviesDao.java", expectedCount % 20, finalCount);
  }
}
