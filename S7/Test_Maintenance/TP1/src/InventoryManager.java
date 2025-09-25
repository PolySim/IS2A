package src;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

  private Map<String, Integer> stockLevels = new HashMap<>();
  private static final int REORDER_THRESHOLD = 5;

  public InventoryManager() {
    // Initial stock levels (could be read from a database in a real-world
    // scenario)
    stockLevels.put("item1", 10);
    stockLevels.put("item2", 3);
    stockLevels.put("item3", 7);
  }

  // Adds stock to the inventory
  public void addStock(String item, int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Invalid quantity. Cannot add stock.");
    }
    int currentStock = stockLevels.getOrDefault(item, 0);
    stockLevels.put(item, currentStock + quantity);
  }

  public int getStock(String item) {
    return stockLevels.getOrDefault(item, 0);
  }

  // Reduces stock for a particular item
  public void removeStock(String item, int quantity) {
    if (this.getStock(item) == 0 || quantity < 0) {
      throw new IllegalArgumentException("Invalid operation. Item not found or quantity invalid.");
    }
    int currentStock = this.getStock(item);
    if (currentStock < quantity) {
      throw new IllegalStateException("Not enough stock to remove. Operation aborted.");
    }
    stockLevels.put(item, currentStock - quantity);
  }

  // Checks stock level for an item
  public boolean isStockLow(String item) {
    return this.getStock(item) != 0 && this.getStock(item) <= REORDER_THRESHOLD;
  }

  // Places an order for an item if stock is low
  public void placeOrderIfNeeded(String item) {
    if (!isStockLow(item) && this.getStock(item) != 0) {
      return;
    }
    addStock(item, 10);
  }

  public String toString() {
    String result = "";
    for (Map.Entry<String, Integer> entry : stockLevels.entrySet()) {
      result += "Item: " + entry.getKey() + ", Stock: " + entry.getValue() + "\n";
    }
    return result;
  }

  // Displays current stock levels (for demonstration purposes)
  public void displayStock() {
    System.out.println(this);
  }
}
