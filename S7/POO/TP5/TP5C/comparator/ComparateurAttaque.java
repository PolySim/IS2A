package comparator;

import java.util.Comparator;

import lib.Pokemon;

public class ComparateurAttaque implements Comparator<Pokemon> {
  public int compare(Pokemon p1, Pokemon p2) {
    return p2.getAttaque() - p1.getAttaque();
  }
}
