package media;

public class OuvrageColl extends Livre {
  private Auteur[] auteurs;


  public OuvrageColl(Auteur[] auteurs, String titre, int prix) {
      super(titre, prix);
      this.auteurs = auteurs;
  }

  public Auteur[] getAuteurs() {
      return this.auteurs;
  }

  
  public String show() {
      String result = "";
      for (Auteur auteur : this.auteurs) {
        result += auteur.show() + " - ";
      }
      return result + super.getTitre() + ", " + super.getPrix();
  }
}
 