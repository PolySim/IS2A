package src;

public class Cpt {

  private int value;

  public Cpt() {
    this.value = 0;
  }

  public synchronized int getValue() {
    return value;
  }

  public synchronized void setValue(int value) {
    this.value = value;
  }

  public synchronized int incrementAndGet() {
    value++;
    return value;
  }
}
