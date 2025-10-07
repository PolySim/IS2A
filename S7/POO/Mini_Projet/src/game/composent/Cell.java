package game.composent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Cell {
  public enum Status {
    VIERGE,
    CLICK,
    FLAG
  }

  private List<Cell> voisins;
  private Status status;
  private final int nb_bombe;

  private final static int MAX_BOMBE = 1;
  private final static double PROBA_BOMBE = 0.05;

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

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Cell> getVoisinsEmptys() {
    Set<Cell> visited = new HashSet<>();
    Queue<Cell> queue = new LinkedList<>();
    List<Cell> result = new ArrayList<>();

    // Ajouter les voisins vides directs
    for (Cell voisin : this.voisins) {
      if (voisin.getStatus() == Status.VIERGE && voisin.getNbBombeVoisins() == 0) {
        queue.add(voisin);
        visited.add(voisin);
      }
    }

    // Parcours en largeur
    while (!queue.isEmpty()) {
      Cell current = queue.poll();
      result.add(current);

      // Ajouter les voisins non visités
      for (Cell voisin : current.voisins) {
        if (!visited.contains(voisin) &&
            voisin.getStatus() == Status.VIERGE &&
            voisin.getNbBombeVoisins() == 0) {
          queue.add(voisin);
          visited.add(voisin);
        }
      }
    }

    return result;
  }

  public String toString() {
    return "Cell [status=" + status + ", nb_bombe=" + nb_bombe + "]";
  }

}
