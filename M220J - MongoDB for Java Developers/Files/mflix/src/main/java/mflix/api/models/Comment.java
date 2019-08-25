package mflix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class Comment {

  @JsonProperty("_id")
  @BsonIgnore
  private String id;

  @BsonId @JsonIgnore private ObjectId oid;

  private String text;

  private Date date;

  private String email;

  private String name;

  @JsonProperty("movie_id")
  @BsonIgnore
  private String movieId;

  @BsonProperty("movie_id")
  @JsonIgnore
  private ObjectId movieObjectId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    this.oid = new ObjectId(id);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMovieId() {
    return movieId;
  }

  public void setMovieId(String movieId) {
    this.movieId = movieId;
    this.movieObjectId = new ObjectId(movieId);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOid(ObjectId oid) {
    this.oid = oid;
    this.id = oid.toHexString();
  }

  public ObjectId getOid() {
    return oid;
  }

  public ObjectId getMovieObjectId() {
    return movieObjectId;
  }

  public void setMovieObjectId(ObjectId movieObjectId) {
    this.movieObjectId = movieObjectId;
    this.movieId = movieObjectId.toHexString();
  }
}
