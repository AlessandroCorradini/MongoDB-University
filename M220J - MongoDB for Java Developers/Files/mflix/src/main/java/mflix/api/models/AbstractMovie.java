package mflix.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractMovie {

  @JsonProperty("_id")
  private String id;

  private String title;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
