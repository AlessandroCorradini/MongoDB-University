package mflix.api.services;

import mflix.api.daos.CommentDao;
import mflix.api.daos.MovieDao;
import mflix.api.daos.MovieDocumentMapper;
import mflix.api.daos.UserDao;
import mflix.api.models.Comment;
import mflix.api.models.Critic;
import mflix.api.models.Movie;
import mflix.api.models.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Configuration
public class MoviesService {

  @Autowired private MovieDao movieDao;
  @Autowired private CommentDao commentDao;
  @Autowired private UserDao userDao;

  public MoviesService() {
    super();
  }

  /**
   * Finds the Movie object that matches the `id` value.
   *
   * @param id - matching movie id.
   * @return Movie object or null if no match applies.
   */
  public Movie getMovie(String id) {

    Movie movie = MovieDocumentMapper.mapToMovie(movieDao.getMovie(id));
    if (movie.getId() == null || movie.getId().isEmpty()) {
      return null;
    }
    return movie;
  }

  /**
   * Checks the a field type class name for a given movie.
   *
   * @param movieId - movie identifier
   * @param fieldKey - document field name.
   * @return Class name of the field type or empty string if field is does not exist.
   */
  public String getMovieDocumentFieldType(String movieId, String fieldKey) {

    Object fieldValue = movieDao.getMovie(movieId).get(fieldKey);
    return fieldValue == null ? "" : fieldValue.getClass().getName();
  }

  /**
   * Lists all movies per page.
   *
   * @param resultsPerPage - number of results per page
   * @param page - result set page
   * @return Map with list of results under `movies_list` key and total count under `movies_count`
   *     key.
   */
  public Map<String, ?> getMovies(int resultsPerPage, int page) {
    int skip = resultsPerPage * page;

    List<Movie> movies =
        movieDao
            .getMovies(resultsPerPage, skip)
            .stream()
            .map(MovieDocumentMapper::mapToMovie)
            .collect(Collectors.toList());
    Map<String, Object> result = new HashMap<>();
    result.put("movies_list", movies);
    if (page == 0) {
      result.put("movies_count", movieDao.getMoviesCount());
    }
    return result;
  }

  /**
   * Finds all countries that have been recorded
   *
   * @param countries - array of countries required to match
   * @return Map containing messages and movies objects that match the countries array
   */
  public Map<String, ?> getMoviesByCountry(String... countries) {

    Map<String, Object> results = new HashMap<>();
    results.put(
        "movies_list",
        movieDao
            .getMoviesByCountry(countries)
            .stream()
            .map(MovieDocumentMapper::mapToMovieTitle)
            .collect(Collectors.toList()));

    return results;
  }

  /**
   * Collects the list of movies that match the Text search for the provided filter.
   *
   * @param resultsPerPage - max number of results per page
   * @param page - wanted page number
   * @param filter - List of keywords to be matched
   * @return Map containing the movies array and total results matching filter criteria.
   */
  public Map<String, ?> getMoviesByText(int resultsPerPage, int page, ArrayList<String> filter) {
    int skip = resultsPerPage * page;
    String keywords = String.join(" ", filter);

    List<Movie> movieList =
        movieDao
            .getMoviesByText(resultsPerPage, skip, keywords)
            .stream()
            .map(MovieDocumentMapper::mapToMovie)
            .collect(Collectors.toList());
    Map<String, Object> result = new HashMap<>();
    result.put("movies_list", movieList);

    if (page == 0) {
      result.put("movies_count", movieDao.getTextSearchCount(keywords));
    }
    return result;
  }

  /**
   * Finds all movies that match the expected cast members.
   *
   * @param resultsPerPage - max number of movies per page
   * @param page - wanted page number
   * @param castFilter - cast to be matched
   * @return Map containing the movies array and total results matching filter criteria.
   */
  public Map<String, ?> getMoviesByCast(
      int resultsPerPage, int page, ArrayList<String> castFilter) {
    int skip = page * resultsPerPage;

    String[] cast = castFilter.toArray(new String[0]);
    String sortKey = "tomatoes.viewer.numReviews";
    List<Movie> movieList =
        movieDao
            .getMoviesByCast(sortKey, resultsPerPage, skip, cast)
            .stream()
            .map(MovieDocumentMapper::mapToMovie)
            .collect(Collectors.toList());

    Map<String, Object> result = new HashMap<>();
    result.put("movies_list", movieList);

    if (page == 0) {
      result.put("movies_count", movieDao.getCastSearchCount(cast));
    }
    return result;
  }

  /**
   * Finds all movies that match the wanted genre.
   *
   * @param resultsPerPage - number of results per page.
   * @param page - page identifier.
   * @param genreFilters - genres filter.
   * @return Map containing the movies array and total results matching filter criteria.
   */
  public Map<String, ?> getMoviesByGenre(
      int resultsPerPage, int page, ArrayList<String> genreFilters) {
    int skip = page * resultsPerPage;

    String[] genres = genreFilters.toArray(new String[0]);
    String sortKey = "tomatoes.viewer.numReviews";
    List<Movie> movieList =
        movieDao
            .getMoviesByGenre(sortKey, resultsPerPage, skip, genres)
            .stream()
            .map(MovieDocumentMapper::mapToMovie)
            .collect(Collectors.toList());

    Map<String, Object> result = new HashMap<>();
    result.put("movies_list", movieList);

    if (page == 0) {
      result.put("movies_count", movieDao.getCastSearchCount(genres));
    }
    return result;
  }

  /**
   * Counts all movies in the database.
   *
   * @return number of movies
   */
  public long getMoviesCount() {
    return movieDao.getMoviesCount();
  }

  /**
   * List of movies matching the faceted search request.
   *
   * @param cast - cast members to be matched
   * @param page - page number
   * @param moviesPerPage - max number of movies per page
   * @return Map containing the movies array, rating buckets, runtime bucket and total count of
   *     results matching filter criteria.
   */
  public Map<String, ?> getMovieFacetedSearch(ArrayList<String> cast, int page, int moviesPerPage) {
    int skip = page * moviesPerPage;
    Map<String, Object> results = new HashMap<>();

    List<Movie> movies = new ArrayList<>();
    Document facetResult =
        movieDao.getMoviesCastFaceted(moviesPerPage, skip, cast.toArray(new String[0])).get(0);

    if (facetResult != null) {
      ArrayList<Document> moviesArray = (ArrayList<Document>) facetResult.get("movies");

      if (moviesArray != null) {
        moviesArray.iterator().forEachRemaining(x -> movies.add(MovieDocumentMapper.mapToMovie(x)));
      }
      results.put("movies", movies);
      results.put("rating", facetResult.get("rating"));
      results.put("runtime", facetResult.get("runtime"));
      results.put("count", movieDao.getCastSearchCount(cast.toArray(new String[0])));
    }
    return results;
  }

  /**
   * Updates an existing movie comment.
   *
   * @param commentBody - Json payload HashMap, containing comment information.
   * @param email - the user email that intends to update the comment.
   * @param results - map with reference to be populated with error message or with movie comments.
   * @return true if request completed, false otherwise.
   */
  public boolean updateMovieComment(
      Map<String, String> commentBody, String email, Map<String, Object> results) {

    Comment updateComment = new Comment();
    updateComment.setMovieId(commentBody.get("movie_id"));
    updateComment.setName(commentBody.getOrDefault("name", ""));
    updateComment.setId(commentBody.get("comment_id"));
    updateComment.setDate(new Date());
    updateComment.setText(commentBody.get("updated_comment"));
    if (!commentDao.updateComment(updateComment.getId(), updateComment.getText(), email)) {
      // check if the email matches the current user
      Comment currentComment = commentDao.getComment(updateComment.getId());
      if (currentComment == null) {
        results.put(
            "error", MessageFormat.format("comment {0} does not exist", updateComment.getId()));
        return false;
      }
      if (!email.equals(currentComment.getEmail())) {
        results.put(
            "error", "Could not update comment. Not authorized to update comments of other users");
        return false;
      }
    }

    Movie movie = MovieDocumentMapper.mapToMovie(movieDao.getMovie(updateComment.getMovieId()));
    results.put("comments", movie.getComments());
    return true;
  }

  /**
   * Add comment to movie.
   *
   * @param movieId - id of movie to comment on.
   * @param email - email of the user that submitted the comment.
   * @param text - comment text.
   * @param results - map with reference to be populated with error message or with movie comments.
   * @return true if comment successfully added to movie.
   */
  public boolean addMovieComment(
      String movieId, String email, String text, HashMap<String, Object> results) {

    Comment newComment = new Comment();
    User user = userDao.getUser(email);
    if (user == null){
      results.put("error", MessageFormat.format("not able to add comment " +
              "for `{0}` email. Not a valid user email", email));
      return false;
    }
    newComment.setOid(new ObjectId());
    newComment.setMovieId(movieId);
    newComment.setEmail(email);
    newComment.setDate(new Date());
    newComment.setText(text);
    newComment.setName(user.getName());

    if (commentDao.addComment(newComment) == null) {
      results.put(
          "error", MessageFormat.format("not able to add comment to movie `{0}` ", movieId));
      return false;
    }

    Movie movie = MovieDocumentMapper.mapToMovie(movieDao.getMovie(movieId));
    results.put("comments", movie.getComments());
    return true;
  }

  /**
   * Deletes a movie comment and returns the updated list of comments for that movie.
   *
   * @param movieId - movie identifier.
   * @param email - email of user intending to delete the comment.
   * @param commentId - comment identifier.
   * @param results - map with reference to be populated with error message or with movie comments.
   * @return true if successful deleting the movie comment.
   */
  public boolean deleteMovieComment(
      String movieId, String email, String commentId, Map<String, Object> results) {

    if (!commentDao.deleteComment(commentId, email)) {
      results.put(
          "error",
          MessageFormat.format("user `{0}` cannot delete comment `{1}`", email, commentId));
      return false;
    }

    Movie movie = MovieDocumentMapper.mapToMovie(movieDao.getMovie(movieId));
    results.put("comments", movie.getComments());
    return true;
  }

  /**
   * Bypass method that returns the list of most active users in terms of comments made in MFlix.
   *
   * @return List of Critic objects.
   */
  public List<Critic> mostActiveUsers() {
    return commentDao.mostActiveCommenters();
  }

  /**
   * Collects the configured pool size and user connection status.
   *
   * @return Map of key value pairs reflecting the configured userInfo, pool_size and wtimeout
   *     settings.
   */
  public Map<String, ?> getConfiguration() {

    return this.movieDao.getConfiguration();
  }
}
