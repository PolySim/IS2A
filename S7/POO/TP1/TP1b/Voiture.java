public class Voiture {
  private int siv;
  // private static int lastSiv = 126186513;
  private static int lastSiv = 114920367;
  // private static int lastSiv = 0;
  private final static int MAX_SIV = 456975999;

  public Voiture() {
    this.initSiv();
  }

  public void initSiv() {
    this.setSiv(lastSiv);
    lastSiv += 1;
    if (lastSiv > MAX_SIV) {
      lastSiv = 0;
    }
  }

  public void setSiv(int siv) {
    this.siv = siv;
  }

  public int getSiv() {
    return this.siv;
  }

  public String toString() {
    String sivFomatted = "";
    sivFomatted = "-" + (char) (65 + ((this.getSiv() / 26) % 26)) + (char) (65 + (this.getSiv() % 26)) + sivFomatted;
    sivFomatted = "-" + String.format("%03d", ((this.getSiv() / (26 * 26)) % 1000)) + sivFomatted;
    sivFomatted = "" + (char) (65 + ((this.getSiv() / (26 * 26 * 26 * 1000)) % 26)) + (char) (65 + ((this.getSiv() / (26 * 26 * 1000)) % 26)) + sivFomatted;
    return sivFomatted;
  }

  public void printSiv() {
    System.out.println(this.toString());
  }
}
