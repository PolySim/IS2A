package media;

public class Poster implements Usable {
  private int prix;

  public Poster(int prix) {
    this.prix = prix;
  }

  public int getPrix() {
    return this.prix;
  }

  public String show() {
    return "Poster : " + this.prix;
  }
}
