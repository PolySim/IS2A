package lib;

import exceptions.NomTropLongException;
import exceptions.NomVideException;
import exceptions.ProblemeCategorieException;

public class Article {
    private String nom;
    private TypeArticle type;
    private double prix;
    private final int MAX_NOM_LENGTH = 10;

    public Article(String newNom, String newTypeName, double newPrix) throws NomVideException {
      TypeArticle newType = null;
      try {
        newType = new TypeArticle(newTypeName);
      } catch (ProblemeCategorieException e) {
        try {
        newType = new TypeArticle("alimentaire");
      } catch (ProblemeCategorieException _e) {}
      }
      this.setType(newType);
      this.setNom(newNom);
      this.setPrix(newPrix);
    }

    public void controleNom(String nom) throws NomVideException {
      if (nom.equals("")) {
        throw new NomVideException();
      }
      if (nom.length() > MAX_NOM_LENGTH) {
        throw new NomTropLongException();
      }
    }

    public String getNom() {
        return this.nom;
    }

    public TypeArticle getType() {
        return this.type;
    }

    public double getPrix() {
        return this.prix;
    }

    public void setNom(String newNom) throws NomVideException {
        this.controleNom(newNom);
        this.nom = newNom;
    }

    public void setType(TypeArticle newType) {
        this.type = newType;
    }

    public void setPrix(double newPrix) {
        this.prix = newPrix;
    }

    public String toString() {
        return "|Nom : " + this.getNom() + "|Type : " + this.getType().getCategorie() + "|Prix : " + this.getPrix() + "|";
    }

}
