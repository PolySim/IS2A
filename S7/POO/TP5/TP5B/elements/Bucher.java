package elements;

public class Bucher {
  private float x;
  private float y;
  private final int id;
  private Buchers voisins;

  public Bucher(float x, float y, Buchers buchers, int id) {
    this.setX(x);
    this.setY(y);
    this.id = id;
    this.voisins = this.getVoisinsFromBuchers(buchers);
    for (Bucher voisin : this.voisins.getBuchers()) {
      voisin.getVoisins().add(this);
    }
  }

  public Bucher(float x, float y) {
    this(x, y, new Buchers(), -1);
  }

  public static double getDistance(Bucher bucher1, Bucher bucher2) {
    return Math.pow(bucher1.getX() - bucher2.getX(), 2) + Math.pow(bucher1.getY() - bucher2.getY(), 2);
  }

  public Buchers getVoisins() {
    return this.voisins;
  }

  public int getId() {
    return this.id;
  }

  private Buchers getVoisinsFromBuchers(Buchers buchers) {
    Buchers voisins = new Buchers();
    for (Bucher bucher : buchers.getBuchers()) {
      if (getDistance(this, bucher) < 1) {
        voisins.add(bucher);
      }
    }
    return voisins;
  }

  public Bucher() {
    this(0, 0, new Buchers(), -1);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public String toString() {
    return "(" + this.getX() + ", " + this.getY() + ")";
  }

  public boolean equals(Bucher bucher) {
    return this.getX() == bucher.getX() && this.getY() == bucher.getY();
  }

}
