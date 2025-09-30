package carte;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import plat.Achetable;
import plat.Plat;

public class Carte implements Serializable, Achetable {
  private Map<String, Plat> plats;

  public Carte() {
    this.plats = new HashMap<>();
  }

  public void add(Plat plat) {
    this.plats.put(plat.getNom(), plat);
  }

  public Plat getPlat(String nom) {
    return this.plats.get(nom);
  }

  public String toString() {
    return plats.values().stream()
        .map(Plat::toString)
        .collect(Collectors.joining("\n"));
  }

  public double getPrix() {
    return this.plats.values().stream()
        .map(Plat::getPrix)
        .reduce(0.0, (acc, current) -> acc + current);
  }
}
