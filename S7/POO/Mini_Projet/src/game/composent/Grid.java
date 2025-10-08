package game.composent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
  private List<List<Cell>> cells;

  public static int SIZE;

  public Grid(int size, int nbBombe) {
    Grid.SIZE = size;
    this.cells = new ArrayList<>();
    this.initGrid(nbBombe);
  }

  private void initGrid(int nbBombe) {
    Cell.reset();
    List<int[]> bombePositions = this.generateBombePositions(nbBombe);
    for (int i = 0; i < Grid.SIZE; i++) {
      List<Cell> row = new ArrayList<>();
      for (int j = 0; j < Grid.SIZE; j++) {
        row.add(new Cell(this.getNbBombe(bombePositions, i, j)));
        if (j > 0) {
          row.get(j).addVoisin(row.get(j - 1));
        }
        if (i > 0) {
          row.get(j).addVoisin(this.cells.get(i - 1).get(j));
          if (j > 0) {
            row.get(j).addVoisin(this.cells.get(i - 1).get(j - 1));
          }
          if (j < Grid.SIZE - 1) {
            row.get(j).addVoisin(this.cells.get(i - 1).get(j + 1));
          }
        }
      }
      this.cells.add(row);
    }
  }

  private int getNbBombe(List<int[]> positions, int x, int y) {
    return (int) positions.stream()
        .filter(position -> position[0] == x && position[1] == y)
        .count();
  }

  private List<int[]> generateBombePositions(int nbBombe) {
    List<int[]> positions = new ArrayList<>();
    Random random = new Random();

    while (positions.size() < nbBombe) {
      int x = random.nextInt(Grid.SIZE);
      int y = random.nextInt(Grid.SIZE);
      positions.add(new int[] { x, y });
    }

    return positions;
  }

  public List<List<Cell>> getCells() {
    return this.cells;
  }

  public Cell getCell(int i, int j) {
    return this.cells.get(i).get(j);
  }
}
