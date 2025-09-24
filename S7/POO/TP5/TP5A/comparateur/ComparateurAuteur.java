package comparateur;

import java.util.Comparator;

import media.Livre;

public class ComparateurAuteur implements Comparator<Livre> {
  public int compare(Livre l1, Livre l2) {
    return l1.getAuteur().compareTo(l2.getAuteur());
  }
}
