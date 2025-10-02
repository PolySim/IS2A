package lib;

public class Gare {
    String libelle;
    String voyageurs;
    String commune;
    String departement;
    Coordonnees c;

    public Gare(String libelle, String voyageurs, String commune, String departement, String cString) {
        this.libelle = libelle;
        this.voyageurs = voyageurs;
        this.commune = commune;
        this.departement = departement;
        String[] coord = cString.substring(1, cString.length() - 1).split(","); // supprimer les " ".
        double x = Double.parseDouble(coord[0]);
        double y = Double.parseDouble(coord[1]);
        this.c = new Coordonnees(x, y);
    }

    public String getLibelle() {
        return this.libelle;
    }

    public String getVoyageurs() {
        return this.voyageurs;
    }

    public String getCommune() {
        return this.commune;
    }

    public String getDepartement() {
        return this.departement;
    }

    public Coordonnees getC() {
        return this.c;
    }

    public double distance(Coordonnees c2) {
        return this.c.distance(c2);
    }

    public double distance(Gare g) {
        return this.distance(g.c);
    }

    public boolean equals(Gare g) {
        return this.libelle.equals(g.libelle);
    }

    public String toString() {
        return "Gare : " + this.libelle + " at " + this.c;
    }
}
