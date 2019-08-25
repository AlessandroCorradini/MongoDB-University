package mflix.api.controllers;

import mflix.api.models.Movie;
import mflix.api.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/movies")
public class MovieController extends ApiController {

  @Autowired private MoviesService moviesService;

  @Value("${api.movies.movies_per_page}")
  private int MOVIES_PER_PAGE = 20;

  public MovieController() {
    super();
  }

  private ResponseEntity<Map> buildOkResponse(Map<String, ?> moviesResults, int page, Map filters) {
    return buildOkResponse(moviesResults, page, filters, "movies");
  }

  private ResponseEntity<Map> buildOkResponse(
      Map<String, ?> moviesResults, int page, Map filters, String resultsKey) {

    Map<String, Object> results = new HashMap<>();
    results.put(resultsKey, moviesResults.get("movies_list"));
    results.put("page", page);
    results.put("entries_per_page", MOVIES_PER_PAGE);
    if (moviesResults.containsKey("movies_count")) {
      results.put("total_results", moviesResults.get("movies_count"));
    }

    results.put("filters", filters);

    return ResponseEntity.ok(results);
  }

  @Override
  ResponseEntity<Map> index() {
    return buildOkResponse(moviesService.getMovies(MOVIES_PER_PAGE, 0), 0, Collections.emptyMap());
  }

  @GetMapping(value = "/id/{movieId}")
  ResponseEntity getMovie(@PathVariable(value = "movieId") String movieId) {
    HashMap<String, Object> result = new HashMap<>();
    Movie movie = moviesService.getMovie(movieId);
    if (movie == null) {
      result.put("error", "Not found");
      return ResponseEntity.badRequest().body(result);
    }

    result.put("movie", movie);
    result.put("api", "java");
    result.put("updated_type", moviesService.getMovieDocumentFieldType(movieId, "lastupdated"));
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/countries")
  public ResponseEntity<Map> moviesByCountry(
      @RequestParam(value = "countries") @Size(min = 1) ArrayList<String> countries) {
    return buildOkResponse(
        moviesService.getMoviesByCountry(countries.toArray(new String[0])),
        0,
        Collections.EMPTY_MAP,
        "titles");
  }

  @GetMapping(value = "/search")
  public ResponseEntity<Map> search(
      @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
      @RequestParam(value = "text", required = false) ArrayList<String> text,
      @RequestParam(value = "cast", required = false) ArrayList<String> cast,
      @RequestParam(value = "genre", required = false) ArrayList<String> genre) {

    Map<String, List<String>> filters = new HashMap<>();
    if (text != null) {
      filters.put("text", text);
      return buildOkResponse(
          moviesService.getMoviesByText(MOVIES_PER_PAGE, page, text), page, filters);
    }

    if (cast != null) {
      filters.put("cast", cast);
      return buildOkResponse(
          moviesService.getMoviesByCast(MOVIES_PER_PAGE, page, cast), page, filters);
    }

    if (genre != null) {
      filters.put("genre", genre);
      return buildOkResponse(
          moviesService.getMoviesByGenre(MOVIES_PER_PAGE, page, genre), page, filters);
    }

    return index();
  }

  @RequestMapping(value = "/facet-search", method = RequestMethod.GET)
  public ResponseEntity<Map> apiSearchMoviesFaceted(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "cast", required = false) @Size(min = 1) ArrayList<String> cast) {

    Map<String, List<String>> filters = new HashMap<>();
    filters.put("cast", cast);
    Map<String, ?> results = moviesService.getMovieFacetedSearch(cast, page, MOVIES_PER_PAGE);

    if (results.get("movies") == null) {
      return ResponseEntity.notFound().build();
    }

    Map<String, Object> facets = new HashMap<>();
    facets.put("runtime", results.get("runtime"));
    facets.put("rating", results.get("rating"));

    HashMap<String, Object> response = new HashMap<>();
    response.put("movies", results.get("movies"));
    response.put("facets", facets);
    response.put("total_results", results.get("count"));
    response.put("entries_per_page", MOVIES_PER_PAGE);
    response.put("filters", filters);
    response.put("page", page);
    return ResponseEntity.ok(response);
  }

  @PutMapping(value = "/comment")
  public ResponseEntity updateMovieComment(
      @RequestHeader("Authorization") String authorizationToken,
      @RequestBody HashMap<String, String> body) {

    Map<String, Object> results = new HashMap<>();
    String email = getEmailFromRequest(authorizationToken);
    if (email == null) {
      results.put("error", "email not found");
      return ResponseEntity.badRequest().body(results);
    }
    if (!moviesService.updateMovieComment(body, email, results)) {
      return ResponseEntity.badRequest().body(results);
    }
    results.put("auth_token", tokenProvider.mintJWTHeader(email));
    return ResponseEntity.ok(results);
  }

  @PostMapping(value = "/comment")
  public ResponseEntity addComment(
      @RequestHeader("Authorization") String authorizationToken,
      @RequestBody HashMap<String, String> body) {
    String email = getEmailFromRequest(authorizationToken);
    HashMap<String, Object> results = new HashMap<>();

    String movieID = body.get("movie_id");
    String comment = body.get("comment");
    if (!moviesService.addMovieComment(movieID, email, comment, results)) {
      return ResponseEntity.badRequest().body(results);
    }

    results.put("auth_token", tokenProvider.mintJWTHeader(email));
    return ResponseEntity.ok(results);
  }

  @DeleteMapping(value = "/comment")
  public ResponseEntity deleteComment(
      @RequestHeader("Authorization") String authorizationToken,
      @RequestBody HashMap<String, String> body) {
    String email = getEmailFromRequest(authorizationToken);
    Map<String, Object> results = new HashMap<>();
    if (email == null) {
      results.put("error", "email not found");
      return ResponseEntity.badRequest().body(results);
    }

    String movie_id = body.get("movie_id");
    String comment_id = body.get("comment_id");
    if (!moviesService.deleteMovieComment(movie_id, email, comment_id, results)) {
      return ResponseEntity.badRequest().body(results);
    }

    return ResponseEntity.ok(results);
  }

  @GetMapping(value = "/config-options")
  public ResponseEntity configOptions() {
    Map<String, ?> results = moviesService.getConfiguration();
    if (results.containsKey("error")) {
      return ResponseEntity.badRequest().body(results);
    }
    return ResponseEntity.ok(results);
  }
}
