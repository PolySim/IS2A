package lib;

public class File extends ListeTab {
  public File(int length) {
    super(length);
  }

  public int pop() {
    int result = this.tab[0];
    for (int i = 0; i < this.getLast() - 1; i++) {
      this.tab[i] = this.tab[i + 1];
    }
    this.setLast(this.getLast() - 1);
    return result;
  }
}
