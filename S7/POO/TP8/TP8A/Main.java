import javax.swing.JFrame;

import media.Auteur;
import media.Livre;
import mediatheque.Bibliotheque;
import mediatheque.BibliothequeGraphique;

class Main {
    public static void main(String[] args) {
        Auteur zola = new Auteur("Zola");
        Bibliotheque b = new Bibliotheque();
        b.add(new Livre(zola, "Nana", 19));
        b.add(new Livre(zola, "Nini", 6));
        b.add(new Livre(new Auteur("Bob"), "Germinal", 24));
        b.add(new Livre(new Auteur("Hugo"), "Claude Gueux", 5));

        BibliothequeGraphique b2 = new BibliothequeGraphique(b);
        b2.setVisible(true);
        b2.setSize(1000, 1000);
        b2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
