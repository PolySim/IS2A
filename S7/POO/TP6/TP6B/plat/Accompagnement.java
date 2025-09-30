package plat;

public class Accompagnement extends Plat {
  public Accompagnement(String nom, double prix) {
    super(nom, prix);
  }

  public String toString() {
    return "Accompagnement : " + super.toString();
  }
}
