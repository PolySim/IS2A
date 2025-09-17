import exceptions.NomTropCourtException;
import media.Auteur;

class Main {
    public static void main(String[] args) {
        try {
            Auteur aut1 = new Auteur("");
            System.out.println(aut1);
        } catch (NomTropCourtException e) {
            System.out.println(e.getMessage());
        }
        try {
            Auteur aut2 = new Auteur("Z");
            System.out.println(aut2);
        } catch (NomTropCourtException e) {
            System.out.println(e.getMessage());
        }
        try {
            Auteur aut3 = new Auteur("Zola");
            System.out.println(aut3);
            aut3.setNom("Zo");
            System.out.println(aut3);
        } catch (NomTropCourtException e) {
            System.out.println(e.getMessage());
        }
    }
}
