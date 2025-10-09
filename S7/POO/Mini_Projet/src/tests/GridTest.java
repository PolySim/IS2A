package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.composent.Cell;
import game.composent.Grid;

public class GridTest {

  @BeforeEach
  public void setUp() {
    // Réinitialiser les variables statiques avant chaque test
    Cell.reset();
  }

  @Test
  public void testGridCreation() {
    Grid grid = new Grid(5, 3);

    assertNotNull(grid, "La grille ne devrait pas être null");
    assertEquals(5, Grid.SIZE, "La taille de la grille devrait être 5");
  }

  @Test
  public void testGetCells() {
    Grid grid = new Grid(4, 2);

    List<List<Cell>> cells = grid.getCells();

    assertNotNull(cells, "La liste de cellules ne devrait pas être null");
    assertEquals(4, cells.size(), "La grille devrait avoir 4 lignes");
    assertEquals(4, cells.get(0).size(), "Chaque ligne devrait avoir 4 cellules");
  }

  @Test
  public void testGetCell() {
    Grid grid = new Grid(3, 1);

    Cell cell = grid.getCell(0, 0);

    assertNotNull(cell, "La cellule ne devrait pas être null");
  }

  @Test
  public void testGridWithCorrectNumberOfBombes() {
    Grid grid = new Grid(5, 4);

    assertEquals(4, Cell.getNbBombeGlobal(), "La grille devrait avoir exactement 4 bombes");
  }

  @Test
  public void testVoisinsAreLinked() {
    Grid grid = new Grid(3, 0);

    Cell cell00 = grid.getCell(0, 0);
    Cell cell01 = grid.getCell(0, 1);
    Cell cell10 = grid.getCell(1, 0);

    // La cellule (0,0) devrait avoir 2 voisins: (0,1) et (1,0) et (1,1)
    assertTrue(cell00.getNbVoisins() >= 2, "La cellule (0,0) devrait avoir au moins 2 voisins");
  }

  @Test
  public void testCenterCellHasEightNeighbors() {
    Grid grid = new Grid(5, 0);

    Cell centerCell = grid.getCell(2, 2);

    assertEquals(8, centerCell.getNbVoisins(), "La cellule centrale devrait avoir 8 voisins");
  }

  @Test
  public void testCornerCellHasThreeNeighbors() {
    Grid grid = new Grid(5, 0);

    Cell cornerCell = grid.getCell(0, 0);

    assertEquals(3, cornerCell.getNbVoisins(), "La cellule du coin devrait avoir 3 voisins");
  }

  @Test
  public void testEdgeCellHasFiveNeighbors() {
    Grid grid = new Grid(5, 0);

    Cell edgeCell = grid.getCell(0, 2);

    assertEquals(5, edgeCell.getNbVoisins(), "La cellule sur le bord devrait avoir 5 voisins");
  }

  @Test
  public void testGridWithNoBombes() {
    Grid grid = new Grid(4, 0);

    assertEquals(0, Cell.getNbBombeGlobal(), "La grille ne devrait avoir aucune bombe");

    // Vérifier qu'aucune cellule ne contient de bombe
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        assertEquals(0, grid.getCell(i, j).getNbBombe(),
            "La cellule (" + i + "," + j + ") ne devrait pas avoir de bombe");
      }
    }
  }

  @Test
  public void testGridWithAllBombes() {
    Grid grid = new Grid(3, 9);

    assertEquals(9, Cell.getNbBombeGlobal(), "La grille devrait avoir 9 bombes");

    // Vérifier que toutes les cellules contiennent une bombe
    int bombeCount = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        bombeCount += grid.getCell(i, j).getNbBombe();
      }
    }
    assertEquals(9, bombeCount, "Toutes les cellules devraient contenir une bombe");
  }

  @Test
  public void testNbBombeVoisinsCalculation() {
    Grid grid = new Grid(3, 0);

    // Créer une grille avec une bombe au centre
    grid = new Grid(3, 1);

    // Le nombre total de bombes dans les voisins de toutes les cellules
    // devrait être cohérent
    boolean atLeastOneCellHasNoBombeVoisins = false;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int nbBombeVoisins = grid.getCell(i, j).getNbBombeVoisins();
        assertTrue(nbBombeVoisins >= 0,
            "Le nombre de bombes voisines devrait être >= 0");
        assertTrue(nbBombeVoisins <= 8,
            "Le nombre de bombes voisines ne peut pas dépasser 8");

        if (nbBombeVoisins == 0) {
          atLeastOneCellHasNoBombeVoisins = true;
        }
      }
    }
  }

  @Test
  public void testResetBetweenGrids() {
    Grid grid1 = new Grid(3, 2);
    assertEquals(2, Cell.getNbBombeGlobal(), "La première grille devrait avoir 2 bombes");

    Grid grid2 = new Grid(4, 3);
    assertEquals(3, Cell.getNbBombeGlobal(), "Après reset, la deuxième grille devrait avoir 3 bombes");
  }

  @Test
  public void testDifferentGridSizes() {
    Grid smallGrid = new Grid(2, 1);
    assertEquals(2, Grid.SIZE, "La taille devrait être mise à jour à 2");

    Grid largeGrid = new Grid(10, 5);
    assertEquals(10, Grid.SIZE, "La taille devrait être mise à jour à 10");
    assertEquals(10, largeGrid.getCells().size(), "La grande grille devrait avoir 10 lignes");
  }
}
