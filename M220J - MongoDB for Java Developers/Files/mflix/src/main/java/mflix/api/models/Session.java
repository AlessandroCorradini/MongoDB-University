package mflix.api.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Session {

  @BsonProperty(value = "user_id")
  private String userId;

  private String jwt;

  public Session() {
    super();
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }
}
