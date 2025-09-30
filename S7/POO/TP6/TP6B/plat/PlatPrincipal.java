package plat;

public class PlatPrincipal extends Plat {
  public PlatPrincipal(String nom, double prix) {
    super(nom, prix);
  }

  public String toString() {
    return "PlatPrincipal : " + super.toString();
  }
}
