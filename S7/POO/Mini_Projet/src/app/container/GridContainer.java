package app.container;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import game.composent.Cell;
import game.composent.Grid;

public class GridContainer extends JPanel {
  Grid grid;
  static HashMap<Cell, CellContainer> cellContainers = new HashMap<>();

  public GridContainer(int gridSize, int nbBombe) {
    super();
    this.grid = new Grid(gridSize, nbBombe);
    this.setLayout(new GridLayout(gridSize, gridSize));

    this.grid.getCells().stream()
        .forEach(row -> {
          row.stream()
              .forEach(cell -> {
                CellContainer cellContainer = new CellContainer(cell);
                GridContainer.cellContainers.put(cell, cellContainer);
                this.add(cellContainer);
              });
        });
  }

  public static boolean isWin() {
    return GridContainer.cellContainers.keySet().stream()
        .allMatch(cell -> cell.getStatus() != Cell.Status.VIERGE || cell.getNbBombe() != 0);
  }
}
