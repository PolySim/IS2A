package lib.Arc;

public class Arcs {
  private Arc[] arcs;
  private int last;

  public Arcs() {
    this.arcs = new Arc[100];
    this.setLast(0);
  }

  public void setLast(int last) {
    this.last = last;
  }

  public int getLast() {
    return this.last;
  }

  public Arc[] getArcs() {
    return this.arcs;
  }

  public void setArcs(Arc[] arcs) {
    this.arcs = arcs;
  }

  public void replace(Arc a, int index) {
    this.arcs[index] = a;
  }

  public void insert(Arc a, int index) {
    for (int i = this.getLast() ; i > index ; i--) {
      this.replace(this.getArcs()[i - 1], i);
    }
    this.replace(a, index);
  }
  
  public String toString() {
    String result = "";
    for (int i = 0; i < this.getLast(); i++) {
      result += this.getArcs()[i].toString() + "\n";
    }
    return result;
  }
  
}
