package game.composent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Cell {
  public enum Status {
    VIERGE,
    CLICK,
    FLAG
  }

  private List<Cell> voisins;
  private Status status;
  private final int nbBombe;

  private static int nbBombeGlobal = 0;
  private static int flagGlobal = 0;

  Cell(boolean isBombe) {
    this.voisins = new ArrayList<>();
    this.status = Status.VIERGE;
    this.nbBombe = isBombe ? 1 : 0;
    Cell.nbBombeGlobal += this.nbBombe;
  }

  private void add(Cell cell) {
    this.voisins.add(cell);
  }

  public void addVoisin(Cell cell) {
    this.voisins.add(cell);
    cell.add(this);
  }

  public int getNbBombe() {
    return this.nbBombe;
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
    if (status == Status.FLAG) {
      Cell.flagGlobal += 1;
    } else if (this.status == Status.FLAG) {
      Cell.flagGlobal -= 1;
    }
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

  public static int getNbBombeGlobal() {
    return Cell.nbBombeGlobal;
  }

  public static int getFlagGlobal() {
    return Cell.flagGlobal;
  }

  public String toString() {
    return "Cell [status=" + status + ", nb_bombe=" + nbBombe + "]";
  }

}
