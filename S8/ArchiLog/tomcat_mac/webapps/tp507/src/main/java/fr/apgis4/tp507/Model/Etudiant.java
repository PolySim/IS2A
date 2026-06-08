package fr.apgis4.tp507.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Etudiant {
  @Id
  private Integer id;
  private String nom;
  private String prenom;
  private Integer age;
  private String groupe;
}
