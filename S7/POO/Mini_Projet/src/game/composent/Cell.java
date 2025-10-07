package game.composent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cell {
  enum Status {
    VIERGE,
    CLICK,
    FLAG
  }

  private List<Cell> voisins;
  private Status status;
  private final int nb_bombe;

  private final static int MAX_BOMBE = 1;
  private final static double PROBA_BOMBE = 0.2;

  Cell() {
    this.voisins = new ArrayList<>();
    this.status = Status.VIERGE;
    this.nb_bombe = generate_bombe();
  }

  private int generate_bombe() {
    int nb_bombe = 0;
    Random random = new Random();
    for (int i = 0; i < MAX_BOMBE; i++) {
      nb_bombe += random.nextDouble() < PROBA_BOMBE ? 1 : 0;
    }
    return nb_bombe;
  }

  private void add(Cell cell) {
    this.voisins.add(cell);
  }

  public void addVoisin(Cell cell) {
    this.voisins.add(cell);
    cell.add(this);
  }

  public int getNbBombe() {
    return this.nb_bombe;
  }

  public int getNbBombeVoisins() {
    return this.voisins.stream()
        .map(Cell::getNbBombe)
        .reduce(0, (acc, current) -> acc + current);
  }

  public int getNbVoisins() {
    return this.voisins.size();
  }

  public String toString() {
    return "Cell [status=" + status + ", nb_bombe=" + nb_bombe + "]";
  }

}
