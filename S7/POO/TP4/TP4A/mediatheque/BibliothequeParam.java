package mediatheque;

import java.util.ArrayList;

import media.Usable;

public class BibliothequeParam<E extends Usable> {
    private ArrayList<E> livres;

    public BibliothequeParam() {
        this.livres = new ArrayList<E>();
    }

    public void add(E newLivre) {
        livres.add(newLivre);
    }

    public int getSomme() {
        int somme = 0;
        for (E l : this.livres) {
            somme += l.getPrix();
        }
        return somme;
    }

    public String show() {
        String m = "BibliothequeParam : ";
        for (E l : this.livres) {
            m += "["+l.show()+"], ";
        }
        return m;
    }

}
