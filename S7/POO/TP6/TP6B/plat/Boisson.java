package plat;

public class Boisson extends Plat {
  public Boisson(String nom, double prix) {
    super(nom, prix);
  }

  public String toString() {
    return "Boisson : " + super.toString();
  }
}
