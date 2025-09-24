package media;

public class Livre implements Comparable<Livre> {
    private Auteur auteur;
    private String titre;
    private int prix;
    private static int somme = 0;


    public Livre(Auteur auteur, String titre, int prix) {
        this.auteur = auteur;
        this.titre = titre;
        this.prix = prix;
        Livre.somme += this.prix;
    }

    public Auteur getAuteur() {
        return this.auteur;
    }

    public void setTitre(String newTitre) {
        this.titre = newTitre;
    }

    public String show() {
        return this.titre + ", " + this.auteur.show() + ", " + this.prix;
    }

    public int getPrix() {
        return this.prix;
    }

    public static int getSomme() {
        return Livre.somme;
    }

    public int compareTo(Livre other) {
        return this.prix - other.prix;
    }
}
