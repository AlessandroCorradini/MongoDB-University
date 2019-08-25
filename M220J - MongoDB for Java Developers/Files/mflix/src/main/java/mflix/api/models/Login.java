package mflix.api.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Login {

  @NotNull @Email private String email;

  @NotNull
  @Size(min = 8)
  private String password;

  public Login() {
    super();
  }

  public Login(String email, String password) {
    super();
    this.email = email;
    this.password = password;
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
