package lib;

import java.util.List;

public class PromoParam<T extends Etudiant> {
  private String nom;
  private List<T> etudiants;

  public PromoParam(String nom, List<T> etudiants) {
    this.nom = nom;
    this.etudiants = etudiants;
  }

  public void add(T etu) {
    this.etudiants.add(etu);
  }

  public String toString() {
    return "Nom : " + this.nom + ", Etudiants : " + this.etudiants.toString();
  }
}
