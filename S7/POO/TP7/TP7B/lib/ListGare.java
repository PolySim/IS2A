package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListGare {
  private List<Gare> gares;

  public ListGare(List<Gare> gares) {
    this.gares = gares;
  }

  public ListGare() {
    this(new ArrayList<>());
  }

  public Set<String> getSetDepartement() {
    return this.gares.stream()
        .map(Gare::getDepartement)
        .collect(Collectors.toSet());
  }

  public List<Gare> getListGaresWhere(String str) {
    return this.gares.stream()
        .filter(gare -> gare.getLibelle().contains(str))
        .collect(Collectors.toList());
  }

  public List<String> getSortedLibelle() {
    return this.gares.stream()
        .map(Gare::getLibelle)
        .sorted()
        .collect(Collectors.toList());
  }

  public int getCountCommunes(String departement) {
    return (int) this.gares.stream()
        .filter(gare -> gare.getDepartement().equals(departement))
        .count();
  }

  private List<Gare> plusProcheGares(Coordonnees c, int limit) {
    return this.gares.stream()
        .sorted((g1, g2) -> (g1.distance(c) - g2.distance(c)) > 0 ? 1 : -1)
        .limit(limit)
        .collect(Collectors.toList());
  }

  public List<Gare> plusProcheGares(Coordonnees c) {
    return this.plusProcheGares(c, 20);
  }

  public Map<String, Gare> cheminExtremite(Coordonnees debut, Coordonnees fin) {
    Map<String, Gare> result = new HashMap<>();
    result.put("debut", this.plusProcheGares(debut, 1).get(0));
    result.put("fin", this.plusProcheGares(fin, 1).get(0));
    return result;
  }

  private double minDistance(Gare gare) {
    return this.gares.stream()
        .distinct()
        .filter(g -> !g.equals(gare))
        .map(g -> g.distance(gare))
        .reduce(null, (acc, curr) -> acc == null || curr < acc ? curr : acc);
  }

  public Gare getGareIsole() {
    return this.gares.stream()
        .distinct()
        .sorted((g1, g2) -> (this.minDistance(g1) - this.minDistance(g2)) < 0 ? 1 : -1)
        .collect(Collectors.toList())
        .get(0);
  }

  public List<Gare> getGareLinked(double rayon, int minSize) {
    return this.gares.stream()
        .filter(gare -> this.gares.stream()
            .filter(g -> g.distance(gare) <= rayon)
            .count() >= minSize)
        .collect(Collectors.toList());
  }

  // private List<Gare> getGareByCommune(String commune) {
  // return this.gares.stream()
  // .filter(gare -> gare.getCommune().equals(commune))
  // .collect(Collectors.toList());
  // }

  // public List<String> getBetterCommune(String departement) {
  // return this.gares.stream()
  // .filter(gare -> gare.getDepartement().equals(departement))
  // .sorted(
  // (g1, g2) -> this.getGareByCommune(g2.getCommune()).size() -
  // this.getGareByCommune(g1.getCommune()).size())
  // .map(Gare::getCommune)
  // .distinct()
  // .collect(Collectors.toList());
  // }

  public List<String> getBetterCommune(String departement) {
    return this.gares.stream()
        .filter(gare -> gare.getDepartement().equals(departement))
        .collect(Collectors.groupingBy(Gare::getCommune, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  public double getNombreMoyenGareParCommune() {
    return this.gares.stream()
        .collect(Collectors.groupingBy(Gare::getCommune, Collectors.counting()))
        .entrySet()
        .stream()
        .mapToDouble(Map.Entry::getValue)
        .average()
        .orElse(0);
  }
}
