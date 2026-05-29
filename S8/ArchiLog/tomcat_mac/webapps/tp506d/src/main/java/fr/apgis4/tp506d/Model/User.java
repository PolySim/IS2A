package fr.apgis4.tp506d.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

  private String username;

  @Id
  private String email;

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
