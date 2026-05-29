package fr.apgis4.tp506d.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Entity
@Getter
@Setter
public class Livre {

  @Id
  private String lno;

  private String category;
  private String titre;

  @ManyToOne
  @JoinColumn(name = "ano")
  private Auteur auteur;

  public String toString() {
    // return titre + " (" + auteur.toString() + ")";
    return titre;
  }
}
