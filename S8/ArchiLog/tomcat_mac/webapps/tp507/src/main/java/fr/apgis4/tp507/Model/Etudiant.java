package fr.apgis4.tp507.Model;

// import org.springframework.data.annotation.Id;

// import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

// @Entity
@Getter
@Setter
public class Etudiant {
  // @Id
  private Long id;
  private String nom;
  private String prenom;
  private int age;
  private String groupe;
}
