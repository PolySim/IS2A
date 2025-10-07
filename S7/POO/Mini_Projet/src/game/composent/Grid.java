package game.composent;

import java.util.ArrayList;
import java.util.List;

public class Grid {
  private List<List<Cell>> cells;

  public final static int SIZE = 10;

  public Grid() {
    this.cells = new ArrayList<>();
    this.initGrid();
  }

  private void initGrid() {
    for (int i = 0; i < SIZE; i++) {
      List<Cell> row = new ArrayList<>();
      for (int j = 0; j < SIZE; j++) {
        row.add(new Cell());
        if (j > 0) {
          row.get(j).addVoisin(row.get(j - 1));
        }
        if (i > 0) {
          row.get(j).addVoisin(this.cells.get(i - 1).get(j));
          if (j > 0) {
            row.get(j).addVoisin(this.cells.get(i - 1).get(j - 1));
          }
          if (j < SIZE - 1) {
            row.get(j).addVoisin(this.cells.get(i - 1).get(j + 1));
          }
        }
      }
      this.cells.add(row);
    }
  }

  public List<List<Cell>> getCells() {
    return this.cells;
  }

  public Cell getCell(int i, int j) {
    return this.cells.get(i).get(j);
  }
}
