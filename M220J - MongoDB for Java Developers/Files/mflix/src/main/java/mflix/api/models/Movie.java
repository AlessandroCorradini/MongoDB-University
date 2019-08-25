package mflix.api.models;

import java.util.Date;
import java.util.List;

public class Movie extends AbstractMovie {

  private int year;
  private List<String> cast;
  private String plog;
  private String fullPlot;
  private Date lastUpdated;
  private String type;
  private String poster;
  private List<String> directors;
  private List<String> writers;
  private IMDB imdb;

  private List<String> countries;
  private List<String> genres;

  private RottenTomatoes tomatoes;

  private List<Comment> comments;

  public Movie() {
    super();
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public List<String> getCast() {
    return cast;
  }

  public void setCast(List<String> cast) {
    this.cast = cast;
  }

  public String getPlog() {
    return plog;
  }

  public void setPlog(String plog) {
    this.plog = plog;
  }

  public String getFullPlot() {
    return fullPlot;
  }

  public void setFullPlot(String fullPlot) {
    this.fullPlot = fullPlot;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getDirectors() {
    return directors;
  }

  public void setDirectors(List<String> directors) {
    this.directors = directors;
  }

  public List<String> getWriters() {
    return writers;
  }

  public void setWriters(List<String> writers) {
    this.writers = writers;
  }

  public IMDB getImdb() {
    return imdb;
  }

  public void setImdb(IMDB imdb) {
    this.imdb = imdb;
  }

  public List<String> getCountries() {
    return countries;
  }

  public void setCountries(List<String> countries) {
    this.countries = countries;
  }

  public RottenTomatoes getTomatoes() {
    return tomatoes;
  }

  public void setTomatoes(RottenTomatoes tomatoes) {
    this.tomatoes = tomatoes;
  }

  public List<String> getGenres() {
    return genres;
  }

  public void setGenres(List<String> genres) {
    this.genres = genres;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}
