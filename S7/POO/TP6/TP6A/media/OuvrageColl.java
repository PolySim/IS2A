package media;

public class OuvrageColl extends Livre {
    private Auteur[] auteurs;

    public OuvrageColl(Auteur[] auteurs, String titre, int prix) {
        super(titre,prix);
        this.auteurs = auteurs;
    }

    public Auteur[] getAuteurs() {
        return this.auteurs;
    }

    // Show
    public String show() {
        String out = this.titre + ", ";
        for (Auteur aut : this.getAuteurs()) {
            out += aut.show() + " - ";
        }
        out += ", " + this.prix;
        return out;
    }
    // FIN Show
}
