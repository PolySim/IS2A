package fr.apgis4.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Auteur {

  @Id
  private String email;

  private String ano;
  private String nom;
  private String prenom;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAno() {
    return ano;
  }

  public void setAno(String ano) {
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

  public String toString() {
    return nom + " " + prenom;
  }
}
