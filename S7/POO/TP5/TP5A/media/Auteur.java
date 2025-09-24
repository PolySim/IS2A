package media;

public class Auteur implements Comparable<Auteur> {
    private String nom;

    public Auteur(String nom) {
        this.nom = nom;
    }

    private String getNom() {
        return this.nom;
    }

    public int compareTo(Auteur other) {
        return this.nom.compareTo(other.nom);
    }

    String show() {
        return "Auteur : " + this.getNom();
    }
}
