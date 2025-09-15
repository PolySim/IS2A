package media;

public class OuvrageMono extends Livre {
  private Auteur auteur;


  public OuvrageMono(Auteur auteur, String titre, int prix) {
      super(titre, prix);
      this.auteur = auteur;
  }

  public Auteur getAuteur() {
      return this.auteur;
  }

  public String show() {
    return super.getTitre() + ", " + this.auteur.show() + ", " + super.getPrix();
  }
}
 