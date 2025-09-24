package comparator;

import java.util.Comparator;

import lib.Pokemon;

public class ComparateurPVMax implements Comparator<Pokemon> {
  public int compare(Pokemon p1, Pokemon p2) {
    return p2.getPVMax() - p1.getPVMax();
  }
}
