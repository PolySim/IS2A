package media;

public class Auteur {
    private String nom;

    public Auteur(String nom) {
        this.nom = nom;
    }

    private String getNom() {
        return this.nom;
    }

    String show() {
        return "Auteur : " + this.getNom();
    }
}
