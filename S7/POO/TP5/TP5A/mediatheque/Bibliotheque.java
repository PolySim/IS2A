package mediatheque;

import java.util.ArrayList;
import java.util.Collections;

import comparateur.ComparateurAuteur;
import media.Livre;

public class Bibliotheque {
    private ArrayList<Livre> livres;
    private int nb;

    public Bibliotheque(int nbMax) {
        this.livres = new ArrayList<Livre>();
        this.nb = 0;
    }

    public void add(Livre newLivre) {
        this.livres.add(newLivre);
        nb = (nb + 1) % this.livres.size();
    }

    public int getSomme() {
        int somme = 0;
        for (Livre l : this.livres) {
            somme += l.getPrix();
        }
        return somme;
    }

    public void sortNatural() {
        Collections.sort(this.livres);
    }

    public void sortAuteur() {
        Collections.sort(this.livres, new ComparateurAuteur());
    }

    public String show() {
        String m = "Bibliotheque : ";
        for (Livre l : this.livres) {
            m += "["+l.show()+"], ";
        }
        return m;
    }

}
