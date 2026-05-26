package fr.apgis4.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;

@Entity
public class Livre {

  @Id
  private String lno;

  private String category;
  private String titre;

  // @ManyToOne
  @JoinColumn(name = "ano")
  private Auteur auteur;

  public Auteur getAuteur() {
    return auteur;
  }

  public void setAuteur(Auteur auteur) {
    this.auteur = auteur;
  }

  public String getLno() {
    return lno;
  }

  public void setLno(String lno) {
    this.lno = lno;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getTitre() {
    return titre;
  }

  public void setTitre(String titre) {
    this.titre = titre;
  }

  public String toString() {
    // return titre + " (" + auteur.toString() + ")";
    return titre;
  }
}
