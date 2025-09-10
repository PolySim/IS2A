package media;

public class Livre {
  private String titre;
  private Auteur auteur = new Auteur();
  private double prix;
  static double somme_prix = 0;

  public Livre(String titre, String auteur, double prix) {
    this.setTitre(titre);
    this.setAuteur(auteur);
    this.setPrix(prix);
    Livre.incrementSommePrix(prix);
  }

  public Livre(String titre, Auteur auteur, double prix) {
    this(titre, auteur.getNom(), prix);
  }

  public Livre() {
    this("", "", 0.0);
  }

  public void setAuteur(String auteur) {
    this.auteur.setNom(auteur);
  }

  public void setTitre(String titre) {
    this.titre = titre;
  }

  public void setPrix(double prix) {
    this.prix = prix;
  }
  
  public Auteur getAuteur() {
    return auteur;
  }

  public String getTitre() {
    return titre;
  }

  public double getPrix() {
    return prix;
  }

  public String show() {
    return "Titre: " + this.getTitre() + " Auteur: " + this.getAuteur().getNom() + " Prix: " + this.getPrix();
  }

  static void incrementSommePrix(double prix) {
    Livre.somme_prix += prix;
  }

  static double getSommePrix() {
    return Livre.somme_prix;
  }
}