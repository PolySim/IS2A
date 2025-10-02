package mediatheque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import media.Auteur;
import media.Livre;

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

    public void sortAuteur() {
        Collections.sort(this.livres,
                (Livre l1, Livre l2) -> l1.getAuteur().equals(l2.getAuteur()) ? l2.getPrix() - l1.getPrix()
                        : l1.getAuteur().getNom().compareTo(l2.getAuteur().getNom()));
    }

    public Set<Auteur> getSetAuteurs() {
        return this.livres.stream()
                .map(Livre::getAuteur)
                .collect(Collectors.toSet());
    }

    public List<Livre> getTop3PrixMini() {
        return this.livres.stream()
                .sorted((l1, l2) -> l1.getPrix() - l2.getPrix())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Livre> getLivresAuteur(String name) {
        return this.livres.stream()
                .filter(l -> l.getAuteur().getNom().equals(name))
                .collect(Collectors.toList());
    }

    public void applyReduction(double reduction) {
        this.livres.stream()
                .forEach(l -> l.applyReduction(reduction));
    }

}
