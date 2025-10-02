package media;

import java.util.*;

public class Auteur implements Comparable<Auteur>{
    private String nom;

    public Auteur(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() {
        return "Auteur : " + this.getNom();
    }

    public int compareTo(Auteur a1) {
        return this.getNom().compareTo(a1.getNom());
    }
}
