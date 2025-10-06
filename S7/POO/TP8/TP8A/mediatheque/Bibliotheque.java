package mediatheque;

import media.Livre;
import java.util.*;

public class Bibliotheque {
    private List<Livre> livres;

    public Bibliotheque() {
        this.livres = new ArrayList<>();
    }

    public void add(Livre newLivre) {
        this.livres.add(newLivre);
    }

    public int getSomme() {
        int somme = 0;
        for (Livre l : this.livres) {
            somme += l.getPrix();
        }
        return somme;
    }

    public List<Livre> getLivres() {
        return this.livres;
    }

    public String toString() {
        String m = "Bibliotheque : ";
        for (Livre l : this.livres) {
            m += "[" + l + "], ";
        }
        return m;
    }

    public void sortNatural() {
        Collections.sort(this.livres);
    }
}
