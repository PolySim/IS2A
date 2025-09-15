package lib;

public class Pile extends ListeTab {
  public Pile(int length) {
    super(length);
  }

  public int pop() {
    int result = this.tab[this.getLast() - 1];
    this.setLast(this.getLast() - 1);
    return result;
  }
}
