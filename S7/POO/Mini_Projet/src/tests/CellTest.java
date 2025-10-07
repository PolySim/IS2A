package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.composent.Cell;
import game.composent.Cell.Status;

public class CellTest {

  @BeforeEach
  public void setUp() {
    // Réinitialiser les variables statiques avant chaque test
    Cell.reset();
  }

  @Test
  public void testCellCreationWithoutBombe() {
    Cell cell = new Cell(false);
    assertEquals(0, cell.getNbBombe(), "Une cellule sans bombe devrait avoir 0 bombe");
    assertEquals(Status.VIERGE, cell.getStatus(), "Une nouvelle cellule devrait avoir le statut VIERGE");
  }

  @Test
  public void testCellCreationWithBombe() {
    Cell cell = new Cell(true);
    assertEquals(1, cell.getNbBombe(), "Une cellule avec bombe devrait avoir 1 bombe");
    assertEquals(Status.VIERGE, cell.getStatus(), "Une nouvelle cellule devrait avoir le statut VIERGE");
  }

  @Test
  public void testAddVoisin() {
    Cell cell1 = new Cell(false);
    Cell cell2 = new Cell(false);

    cell1.addVoisin(cell2);

    assertEquals(1, cell1.getNbVoisins(), "cell1 devrait avoir 1 voisin");
    assertEquals(1, cell2.getNbVoisins(), "cell2 devrait avoir 1 voisin (relation bidirectionnelle)");
  }

  @Test
  public void testGetNbBombeVoisins() {
    Cell cell1 = new Cell(false);
    Cell cell2 = new Cell(true);
    Cell cell3 = new Cell(true);
    Cell cell4 = new Cell(false);

    cell1.addVoisin(cell2);
    cell1.addVoisin(cell3);
    cell1.addVoisin(cell4);

    assertEquals(2, cell1.getNbBombeVoisins(), "cell1 devrait avoir 2 bombes dans ses voisins");
  }

  @Test
  public void testSetStatusToClick() {
    Cell cell = new Cell(false);
    cell.setStatus(Status.CLICK);

    assertEquals(Status.CLICK, cell.getStatus(), "Le statut de la cellule devrait être CLICK");
  }

  @Test
  public void testSetStatusToFlag() {
    Cell cell = new Cell(false);
    cell.setStatus(Status.FLAG);

    assertEquals(Status.FLAG, cell.getStatus(), "Le statut de la cellule devrait être FLAG");
    assertEquals(1, Cell.getFlagGlobal(), "Le compteur global de drapeaux devrait être 1");
  }

  @Test
  public void testSetStatusFromFlagToVierge() {
    Cell cell = new Cell(false);
    cell.setStatus(Status.FLAG);
    assertEquals(1, Cell.getFlagGlobal(), "Le compteur global de drapeaux devrait être 1");

    cell.setStatus(Status.VIERGE);
    assertEquals(0, Cell.getFlagGlobal(), "Le compteur global de drapeaux devrait revenir à 0");
  }

  @Test
  public void testGetNbBombeGlobal() {
    Cell cell1 = new Cell(true);
    Cell cell2 = new Cell(true);
    Cell cell3 = new Cell(false);

    assertEquals(2, Cell.getNbBombeGlobal(), "Il devrait y avoir 2 bombes au total");
  }

  @Test
  public void testGetFlagGlobal() {
    Cell cell1 = new Cell(false);
    Cell cell2 = new Cell(false);
    Cell cell3 = new Cell(false);

    cell1.setStatus(Status.FLAG);
    cell2.setStatus(Status.FLAG);

    assertEquals(2, Cell.getFlagGlobal(), "Il devrait y avoir 2 drapeaux au total");
  }

  @Test
  public void testReset() {
    Cell cell1 = new Cell(true);
    Cell cell2 = new Cell(true);
    cell1.setStatus(Status.FLAG);

    Cell.reset();

    assertEquals(0, Cell.getNbBombeGlobal(), "Le compteur global de bombes devrait être réinitialisé à 0");
    assertEquals(0, Cell.getFlagGlobal(), "Le compteur global de drapeaux devrait être réinitialisé à 0");
  }

  @Test
  public void testGetVoisinsEmptys() {
    Cell cell1 = new Cell(false);
    Cell cell2 = new Cell(false);
    Cell cell3 = new Cell(false);

    cell1.addVoisin(cell2);
    cell2.addVoisin(cell3);

    var voisinsEmptys = cell1.getVoisinsEmptys();

    assertTrue(voisinsEmptys.contains(cell2), "Les voisins vides devraient contenir cell2");
    assertTrue(voisinsEmptys.contains(cell3), "Les voisins vides devraient contenir cell3 (voisin de voisin)");
  }
}
