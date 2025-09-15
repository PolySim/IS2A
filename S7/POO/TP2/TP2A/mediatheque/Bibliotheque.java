package mediatheque;

import media.Livre;

public class Bibliotheque {
    private Livre[] livres;
    private int nb;

    public Bibliotheque(int nbMax) {
        this.livres = new Livre[nbMax];
        this.nb = 0;
    }

    public void add(Livre newLivre) {
        this.livres[nb] = newLivre;
        nb = (nb + 1) % this.livres.length;
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
