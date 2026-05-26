package fr.apgis4.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getAno() {
    return ano;
  }

  public void setAno(Integer ano) {
    this.ano = ano;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

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

  public Set<Livre> getLivres() {
    return livres;
  }

  public void setLivres(Set<Livre> livres) {
    this.livres = livres;
  }
}
