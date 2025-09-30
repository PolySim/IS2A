package media;

import java.io.Serializable;

public abstract class Livre implements Serializable {
    protected String titre;
    protected int prix;
    private static int somme = 0;

    public Livre(String titre, int prix) {
        this.titre = titre;
        this.prix = prix;
        Livre.somme += this.prix;
    }

    public void setTitre(String newTitre) {
        this.titre = newTitre;
    }

    public int getPrix() {
        return this.prix;
    }

    public static int getSomme() {
        return Livre.somme;
    }

    public abstract String show();

}
