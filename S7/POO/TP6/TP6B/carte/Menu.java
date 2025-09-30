package carte;

import plat.Accompagnement;
import plat.Achetable;
import plat.Boisson;
import plat.PlatPrincipal;

public class Menu implements Achetable {
  private Boisson boisson;
  private PlatPrincipal platPrincipal;
  private Accompagnement accompagnement;
  private final static double DISCOUNT = 0.3;

  public Menu(Boisson boisson, PlatPrincipal platPrincipal, Accompagnement accompagnement) {
    this.boisson = boisson;
    this.platPrincipal = platPrincipal;
    this.accompagnement = accompagnement;
  }

  public double getPrix() {
    return (this.platPrincipal.getPrix() + this.accompagnement.getPrix() + this.boisson.getPrix())
        * (1 - Menu.DISCOUNT);
  }

  public String toString() {
    return "Menu complet (" + this.platPrincipal.getNom() + " + " +
        this.accompagnement.getNom() + " + " + this.boisson.getNom() + ")";
  }
}
