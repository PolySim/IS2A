package elements;

public class Point {
  private float x;
  private float y;

  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public String toString() {
    return "(" + this.getX() + ", " + this.getY() + ")";
  }

  public boolean equals(Point point) {
    return this.getX() == point.getX() && this.getY() == point.getY();
  }
}
