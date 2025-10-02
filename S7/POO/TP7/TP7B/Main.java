import lib.ListGare;
import utils.VoirGare;

public class Main {
  public static void main(String[] args) {
    VoirGare vg = new VoirGare("liste_gare_short.csv");
    // vg.voirGare();

    ListGare lg = new ListGare(vg.getGares());
    // System.out.println(lg.getSetDepartement());
    // System.out.println(lg.getListGaresWhere("St"));
    // System.out.println(lg.getSortedLibelle());
    // System.out.println(lg.getCountCommunes("Ille-et-Vilaine"));
    // System.out.println(lg.plusProcheGares(new Coordonnees(47.8223131529,
    // -1.81855289434)));
    // System.out
    // .println(
    // lg.cheminExtremite(new Coordonnees(47.8223131529, -1.81855289434),
    // new Coordonnees(47.6785052299, -2.96058097337)));
    // System.out.println(lg.getGareIsole());
    // System.out.println(lg.getGareLinked(0.2, 5));
    // System.out.println(lg.getBetterCommune("Ille-et-Vilaine"));
    System.out.println(lg.getNombreMoyenGareParCommune());
  }
}
