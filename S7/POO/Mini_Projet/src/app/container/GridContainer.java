package app.container;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import game.composent.Cell;
import game.composent.Grid;

public class GridContainer extends JPanel {
  Grid grid = new Grid();
  static HashMap<Cell, CellContainer> cellContainers = new HashMap<>();

  public GridContainer() {
    super();
    this.setLayout(new GridLayout(Grid.SIZE, Grid.SIZE));

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
}
