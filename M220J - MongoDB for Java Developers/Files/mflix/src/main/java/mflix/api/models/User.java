package mflix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class User {

  private String name;
  private String email;
  @JsonIgnore private String hashedpw;

  private boolean isAdmin;

  private Map<String, String> preferences;

  public User() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashedpw() {
    return hashedpw;
  }

  public void setHashedpw(String hashedpw) {
    this.hashedpw = hashedpw;
  }

  public Map<String, String> getPreferences() {
    return preferences;
  }

  public void setPreferences(Map<String, String> preferences) {
    this.preferences = preferences;
  }

  /**
   * Checks for user object is emptiness.
   *
   * @return if no email set, the user is empty.
   */
  @JsonIgnore
  public boolean isEmpty() {
    return this.email == null || "".equals(this.getEmail());
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }
}
