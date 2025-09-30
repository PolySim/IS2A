import java.util.ArrayList;

import media.Auteur;
import media.Livre;
import media.OuvrageColl;
import media.OuvrageMono;
import mediatheque.Bibliotheque;

class Main {
    public static void main(String[] args) {

        Auteur zola = new Auteur("Zola");
        OuvrageMono nana = new OuvrageMono(zola, "Nana", 19);
        Auteur[] auts = { new Auteur("Hugo"), zola };

        ArrayList<Livre> livres = new ArrayList<Livre>();
        livres.add(nana);
        livres.add(new OuvrageMono(zola, "Germinal", 24));
        livres.add(new OuvrageColl(auts, "Claude Gueux", 5));

        // Bibliotheque b = new Bibliotheque();
        // b.read();
        // b.write(livres);
        // System.out.println(b.show());
        Bibliotheque b = new Bibliotheque("save.bin");
        System.out.println(b.show());
    }
}
