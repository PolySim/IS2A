package media;

public class Auteur {
  private String nom;

  public Auteur(String nom) {
    this.setNom(nom);
  }

  public Auteur() {
    this("");
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public String show() {
    return "Nom: " + this.getNom();
  }
}
