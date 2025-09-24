import media.Auteur;
import media.Livre;
import mediatheque.Bibliotheque;

class Main {
    public static void main(String[] args) {
        Auteur zola = new Auteur("Zola");
        Auteur hugo = new Auteur("Hugo");
        Bibliotheque b = new Bibliotheque();
        b.add(new Livre(zola, "Nana",19));
        b.add(new Livre(hugo, "Germinal",24));
        // System.out.println(b.getSomme());
        System.out.println(b.show());
        b.sortAuteur();
        System.out.println(b.show());
        b.sortNatural();
        System.out.println(b.show());
        b.add(new Livre(new Auteur("Hugo"), "Claude Gueux",5));
        // System.out.println(b.getSomme());
        System.out.println(b.show());
        b.sortNatural();
        System.out.println(b.show());
        b.sortAuteur();
        System.out.println(b.show());
    }
}
