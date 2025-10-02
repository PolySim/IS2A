package lib;

public class Coordonnees {
    double x;
    double y;

    public Coordonnees(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Coordonnees c) {
        return Math.sqrt(Math.pow(this.x - c.x,2) + Math.pow(this.y - c.y,2));
    }

    public String toString() {
        return "{"+this.x+", "+this.y+"}";
    }
}
