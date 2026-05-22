package src;

public class AtomicInteger {

  private int value;

  public AtomicInteger() {
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
