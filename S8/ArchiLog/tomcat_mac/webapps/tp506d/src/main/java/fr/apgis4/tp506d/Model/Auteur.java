package fr.apgis4.tp506d.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Entity
@Getter
@Setter
public class Auteur {

  private String email;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer ano;

  @Column(length = 64)
  private String nom;

  private String prenom;

  @OneToMany(mappedBy = "auteur")
  private Set<Livre> livres;

  public String printAuteurWithLivre() {
    String res = this.nom + " " + this.prenom + "\n Livres :\n";
    String livres = this.getLivres()
      .stream()
      .map(livre -> " - " + livre.getTitre() + "\n")
      .reduce("", String::concat);
    return res + livres;
  }

  public String toString() {
    return nom + " " + prenom;
  }
}
