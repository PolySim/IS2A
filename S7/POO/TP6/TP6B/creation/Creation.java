package creation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import carte.Carte;
import plat.Plat;

public class Creation {
  List<Plat> plats;
  public final static String FILENAME = "creation.bin";

  public Creation() {
    this.plats = new LinkedList<Plat>();
  }

  public void add(Plat plat) {
    this.plats.add(plat);
  }

  public Carte createCarte() {
    Carte carte = new Carte();
    plats.stream()
        .forEach(carte::add);
    return carte;
  }

  public String toString() {
    return plats.stream()
        .map(Plat::toString)
        .collect(Collectors.joining("\n"));
  }

  public void save(Carte carte) {
    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILENAME));
      out.writeObject(carte);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
