package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import src.InventoryManager;

public class InventoryManagerTest {
  private InventoryManager inventoryManager = new InventoryManager();

  @Test
  public void testAddStockValid() {
    int currentStock = inventoryManager.getStock("item1");
    inventoryManager.addStock("item1", 1);
    assertEquals(currentStock + 1, inventoryManager.getStock("item1"));
  }

  @Test
  public void testAddStockInvalid() {
    assertThrows(IllegalArgumentException.class, () -> inventoryManager.addStock("item1", -1));
  }

  @Test
  public void testRemoveStockValid() {
    inventoryManager.addStock("item1", 1);
    int currentStock = inventoryManager.getStock("item1");
    inventoryManager.removeStock("item1", 1);
    assertEquals(currentStock - 1, inventoryManager.getStock("item1"));
  }

  @Test
  public void testRemoveStockNegative() {
    inventoryManager.addStock("item1", 1);
    assertThrows(IllegalArgumentException.class, () -> inventoryManager.removeStock("item1", -1));
  }

  @Test
  public void testRemoveStockNotContained() {
    assertThrows(IllegalArgumentException.class, () -> inventoryManager.removeStock("notContained", 1));
  }

  @Test
  public void testRemoveStockNotStock() {
    inventoryManager.addStock("newItem", 1);
    assertThrows(IllegalStateException.class, () -> inventoryManager.removeStock("newItem", 2));
  }

  @Test
  public void testIsStockSlowValid() {
    assertTrue(inventoryManager.isStockLow("item2"));
  }

  @Test
  public void testIsStockSlowNotValid() {
    assertFalse(inventoryManager.isStockLow("item1"));
  }

  @Test
  public void testIsStockSlowNotContained() {
    assertFalse(inventoryManager.isStockLow("notContained"));
  }

  @Test
  public void testPlaceOrderIfNeededValid() {
    int current = inventoryManager.getStock("item2");
    inventoryManager.placeOrderIfNeeded("item2");
    assertTrue(current != inventoryManager.getStock("item2"));
  }

  @Test
  public void testPlaceOrderIfNeededNotValid() {
    int current = inventoryManager.getStock("item1");
    inventoryManager.placeOrderIfNeeded("item1");
    assertTrue(current == inventoryManager.getStock("item1"));
  }

  @Test
  public void testPlaceOrderIfNeededNotContained() {
    inventoryManager.placeOrderIfNeeded("notContained");
    assertTrue(0 != inventoryManager.getStock("notContained"));
  }

}
