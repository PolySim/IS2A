package lib;

public class File extends ListeTab {
  public File() {
    super();
  }

  public int pop() {
    int result = this.getTab()[0];
    int[] newTab = new int[this.getSize()];
    for (int i = 0; i < this.getLast() - 1; i++) {
      newTab[i] = this.getTab()[i + 1];
    }
    this.setTab(newTab);
    this.setLast(this.getLast() - 1);
    return result;
  }

  public void expands() {
    int[] newTab = new int[this.getSize() * 2];
    for (int i = 0; i < this.getSize() ; i++) {
      newTab[i] = this.getTab()[i];
    }
    this.setSize(this.getSize() * 2);
    this.setTab(newTab);
  }

  public boolean isFull() {
    return this.getLast() + 1 == this.getSize();
  }

  public String toString() {
    String out = "";
    for (int i = 0; i < this.getLast(); i++) {
      out += this.getTab()[i] + " ";
    }
    return out;
  }
}