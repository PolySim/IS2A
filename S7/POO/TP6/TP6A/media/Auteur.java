package media;

import java.io.Serializable;

public class Auteur implements Serializable {
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
