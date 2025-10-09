package app.container;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

import game.composent.Cell;
import game.composent.Grid;
import game.composent.Timer;

public class GridContainer extends JPanel {
  Grid grid;
  public static HashMap<Cell, CellContainer> cellContainers = new HashMap<>();
  private static boolean isStarted = false;
  private static Timer gameTimer = null;

  public GridContainer(int gridSize, int nbBombe, Timer timer) {
    super();
    GridContainer.setIsStarted(false);
    GridContainer.gameTimer = timer;
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

  public static void setIsStarted(boolean isStarted) {
    GridContainer.isStarted = isStarted;
    // Démarrer le timer quand le jeu commence
    if (isStarted && GridContainer.gameTimer != null) {
      HeaderContainer.startTimerInstance();
    }
  }

  public static boolean getIsStarted() {
    return GridContainer.isStarted;
  }

  public static game.composent.Timer getGameTimer() {
    return GridContainer.gameTimer;
  }

  public static void moveBombe(Cell cell) {
    cell.resetNbBombe();
    int indexRandom = new Random().nextInt(Grid.SIZE * Grid.SIZE);
    Cell cellRandom = GridContainer.cellContainers.keySet().stream().toList().get(indexRandom);
    // Compare l'adresse mémoire de la cellule
    while (cellRandom == cell) {
      indexRandom = new Random().nextInt(Grid.SIZE * Grid.SIZE);
      cellRandom = GridContainer.cellContainers.keySet().stream().toList().get(indexRandom);
    }
    cellRandom.incrementNbBombe();
  }

  public static boolean isWin() {
    return GridContainer.cellContainers.keySet().stream()
        .allMatch(cell -> cell.getStatus() != Cell.Status.VIERGE || cell.getNbBombe() != 0);
  }
}
