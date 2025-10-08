package game.composent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import app.container.GridContainer;
import app.container.HeaderContainer;

public class Cell {
  public enum Status {
    VIERGE,
    CLICK,
    FLAG
  }

  private List<Cell> voisins;
  private Status status;
  private int nbBombe;
  private int nbFlag;

  private static int nbBombeGlobal = 0;
  private static int flagGlobal = 0;

  public Cell(int nbBombe) {
    this.voisins = new ArrayList<>();
    this.status = Status.VIERGE;
    this.nbBombe = nbBombe;
    this.nbFlag = 0;
    Cell.nbBombeGlobal += this.nbBombe;
  }

  private void add(Cell cell) {
    this.voisins.add(cell);
  }

  public void addVoisin(Cell cell) {
    this.add(cell);
    cell.add(this);
  }

  public int getNbBombe() {
    return this.nbBombe;
  }

  public void resetNbBombe() {
    this.nbBombe = 0;
  }

  public void incrementNbBombe() {
    this.nbBombe += 1;
  }

  public int getNbBombeVoisins() {
    return this.voisins.stream()
        .map(Cell::getNbBombe)
        .reduce(0, (acc, current) -> acc + current);
  }

  public int getNbVoisins() {
    return this.voisins.size();
  }

  public int getNbFlag() {
    return this.nbFlag;
  }

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    if (status == Status.FLAG) {
      this.nbFlag += 1;
      Cell.flagGlobal += 1;
      this.status = Status.FLAG;
      return;
    }
    if (this.status == Status.FLAG && status == Status.VIERGE) {
      this.nbFlag -= 1;
      Cell.flagGlobal -= 1;
      if (this.nbFlag == 0) {
        this.status = Status.VIERGE;
      }
      return;
    }
    if (status == Status.CLICK) {
      Cell.flagGlobal -= this.nbFlag;
      this.nbFlag = 0;
      HeaderContainer.updateInstance();
      GridContainer.cellContainers.get(this).label
          .setText(String.valueOf(this.getNbBombeVoisins() > 0 ? this.getNbBombeVoisins() : ""));
    }
    this.status = status;
  }

  public Set<Cell> getVoisinsEmptys() {
    Set<Cell> visited = new HashSet<>();
    Queue<Cell> queue = new LinkedList<>();
    List<Cell> resultParcours = new ArrayList<>();
    Set<Cell> result = new HashSet<>();

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
      resultParcours.add(current);

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

    result.addAll(resultParcours);

    resultParcours.stream()
        .forEach(c -> {
          result.addAll(c.voisins);
        });

    return result;
  }

  public static int getNbBombeGlobal() {
    return Cell.nbBombeGlobal;
  }

  public static int getFlagGlobal() {
    return Cell.flagGlobal;
  }

  public static void reset() {
    Cell.nbBombeGlobal = 0;
    Cell.flagGlobal = 0;
  }

  public String toString() {
    return "Cell [status=" + status + ", nb_bombe=" + nbBombe + "]";
  }

}
