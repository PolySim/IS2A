package plat;

import java.io.Serializable;

public abstract class Plat implements Serializable, Achetable {
  private String nom;
  private double prix;

  public Plat(String nom, double prix) {
    this.setNom(nom);
    this.setPrix(prix);
  }

  public String getNom() {
    return this.nom;
  }

  public double getPrix() {
    return this.prix;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setPrix(double prix) {
    this.prix = prix;
  }

  public String toString() {
    return this.nom + " " + this.prix + "€";
  }

}
