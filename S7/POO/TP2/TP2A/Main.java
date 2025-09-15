import media.Auteur;
import media.OuvrageColl;
import media.OuvrageMono;
import mediatheque.Bibliotheque;

class Main {
    public static void main(String[] args) {
        Auteur zola = new Auteur("Zola");
        Bibliotheque b = new Bibliotheque(2);
        b.add(new OuvrageMono(zola, "Nana",19));
        b.add(new OuvrageMono(zola, "Germinal",24));
        System.out.println(b.getSomme());
        System.out.println(b.show());
        b.add(new OuvrageColl(new Auteur[] {new Auteur("Hugo")}, "Claude Gueux",5));
        System.out.println(b.getSomme());
        System.out.println(b.show());
    }
}
