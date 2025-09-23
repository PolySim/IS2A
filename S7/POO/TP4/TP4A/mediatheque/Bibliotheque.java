package mediatheque;

import media.Livre;

import java.util.ArrayList;

public class Bibliotheque {
    private ArrayList<Livre> livres;

    public Bibliotheque() {
        this.livres = new ArrayList<Livre>();
    }

    public void add(Livre newLivre) {
        livres.add(newLivre);
    }

    public int getSomme() {
        int somme = 0;
        for (Livre l : this.livres) {
            somme += l.getPrix();
        }
        return somme;
    }

    public String show() {
        String m = "Bibliotheque : ";
        for (Livre l : this.livres) {
            m += "["+l.show()+"], ";
        }
        return m;
    }

}
