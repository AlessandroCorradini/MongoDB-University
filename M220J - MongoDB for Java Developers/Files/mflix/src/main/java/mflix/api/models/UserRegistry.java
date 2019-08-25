package mflix.api.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegistry {

  @NotNull(message = "`name` field is mandatory")
  @Size(min = 3, message = "`name` must be at least 3 characters long")
  private String name;

  @NotNull(message = "`email` field is mandatory")
  @Email(message = "`email` must be an well-formed email address")
  private String email;

  @NotNull(message = "`password` field is mandatory")
  @Size(min = 8, message = "`password` must be at least 8 characters long")
  private String password;

  public UserRegistry() {}

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
