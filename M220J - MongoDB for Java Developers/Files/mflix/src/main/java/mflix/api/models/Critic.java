package mflix.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Critic {

  @JsonProperty("_id")
  private String id;

  @JsonProperty("count")
  @BsonProperty("count")
  private int numComments;

  public Critic() {
    super();
  }

  public Critic(String id, int numComments) {
    super();
    this.id = id;
    this.numComments = numComments;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getNumComments() {
    return numComments;
  }

  public void setNumComments(int numComments) {
    this.numComments = numComments;
  }
}
