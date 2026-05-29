package fr.apgis4.tp506c.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "users")
public class User {

  private String username;

  @Id
  private String email;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String toString() {
    return (
      "User{" +
      "username='" +
      username +
      '\'' +
      ", email='" +
      email +
      '\'' +
      '}'
    );
  }
}
