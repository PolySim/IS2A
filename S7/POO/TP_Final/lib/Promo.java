package lib;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.PasMajeurException;

public class Promo {
  private String nom;
  private List<Etudiant> etudiants;

  public Promo(String nom, List<Etudiant> etudiants) {
    this.nom = nom;
    this.etudiants = etudiants;
  }

  public void add(Etudiant etu) {
    this.etudiants.add(etu);
  }

  public void sortEByNom() {
    Collections.sort(this.etudiants, (e1, e2) -> e1.getNom().compareTo(e2.getNom()));
  }

  public Set<Etudiant> getCEByAge() {
    return this.etudiants.stream()
        .filter(etu -> etu instanceof ChatEtudiant) // Vérifie si le type dynamique correspond bien
        .sorted((etu1, etu2) -> {
          try {
            return etu1.getAge() - etu2.getAge();
          } catch (PasMajeurException e) {
            return 0;
          }
        })
        .collect(Collectors.toSet());
  }

  public Map<Integer, List<Etudiant>> getMapAgeToEs() {
    return this.etudiants.stream()
        .collect(Collectors.groupingBy(arg0 -> {
          try {
            return arg0.getAge();
          } catch (PasMajeurException e) {
            return 0;
          }
        }));
  }

  public String toString() {
    return "Nom : " + this.nom + ", Etudiants : " + this.etudiants.toString();
  }
}
