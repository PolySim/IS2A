package mediatheque;

import media.Livre;

public class Bibliotheque {
  private Livre[] livres;

  public Bibliotheque(Livre[] livre) {
    this.setLivres(livre);
  }

  public Bibliotheque() {
    this.setLivres(new Livre[0]);
  }

  public void setLivres(Livre[] livre) {
    this.livres = livre;
  }

  public Livre[] getLivres() {
    return this.livres;
  }

  public int getSommePrix() {
    int somme = 0;
    for (Livre livre : this.livres) {
      somme += livre.getPrix();
    }
    return somme;
  }

  public String show() {
    String result = "";
    for (Livre livre : this.livres) {
      result += livre.show() + "\n";
    }
    return result;
  }
}
