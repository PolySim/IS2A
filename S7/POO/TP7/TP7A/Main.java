import media.Auteur;
import media.Livre;
import mediatheque.Bibliotheque;

class Main {
    public static void main(String[] args) {
        Auteur zola = new Auteur("Zola");
        Bibliotheque b = new Bibliotheque();
        b.add(new Livre(zola, "Nana", 19));
        b.add(new Livre(zola, "Nini", 6));
        b.add(new Livre(new Auteur("Bob"), "Germinal", 24));
        b.add(new Livre(new Auteur("Hugo"), "Claude Gueux", 5));
        // System.out.println(b);

        // b.sortAuteur();
        // System.out.println(b);
        // System.out.println(b.getSetAuteurs());
        // System.out.println(b.getTop3PrixMini());
        // System.out.println(b.getLivresAuteur("Zola"));
        b.applyReduction(2);
        System.out.println(b);
    }
}
