package media;

abstract public class Livre {
    private String titre;
    private int prix;
    private static int somme = 0;


    public Livre(String titre, int prix) {
        this.titre = titre;
        this.prix = prix;
        Livre.somme += this.prix;
    }
    
    public void setTitre(String newTitre) {
        this.titre = newTitre;
    }

    public String getTitre() {
        return this.titre;
    }

    public int getPrix() {
        return this.prix;
    }

    public static int getSomme() {
        return Livre.somme;
    }

    abstract public String show();

}
