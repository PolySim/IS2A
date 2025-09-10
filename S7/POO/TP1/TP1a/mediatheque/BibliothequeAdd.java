package mediatheque;

import media.Livre;

public class BibliothequeAdd {
  private Livre[] livres;
  private final static int MAX_LIVRES = 3;

  public BibliothequeAdd(Livre[] livre) {
    this.setLivres(livre);
  }

  public BibliothequeAdd() {
    this(new Livre[MAX_LIVRES]);
  }

  public void setLivres(Livre[] livre) {
    this.livres = livre;
  }

  public Livre[] getLivres() {
    return this.livres;
  }

  public void addLivre(Livre livre) {
    if (this.livres.length < MAX_LIVRES) {
      this.livres[this.livres.length] = livre;
    } else {
      for (int i = 0; i < this.livres.length - 1; i++) {
        this.livres[i] = this.livres[i + 1];
      }
      this.livres[this.livres.length - 1] = livre;
    }
  }

  public void removeLivre(int index) {
    for (int i = index; i < this.livres.length - 1; i++) {
      this.livres[i] = this.livres[i + 1];
    }
    this.livres[this.livres.length - 1] = null;
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
      if (livre != null) {
        result += livre.show() + "\n";
      }
    }
    return result;
  }
}
