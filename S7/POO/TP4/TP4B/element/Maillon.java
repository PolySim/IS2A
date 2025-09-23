package element;

public class Maillon<T> {
  private T valeur;
  private Maillon<T> queue;

  public Maillon(T valeur, Maillon<T> queue) {
    this.setValeur(valeur);
    this.setQueue(queue);
  }

  public Maillon(T valeur) {
    this.valeur = valeur;
    this.queue = null;
  }

  public T getValeur() {
    return this.valeur;
  }

  public void setValeur(T valeur) {
    this.valeur = valeur;
  }

  public Maillon<T> getQueue() {
    return this.queue;
  }

  public void setQueue(Maillon<T> queue) {
    this.queue = queue;
  }

  public String toString() {
    return "Maillon: " + this.getValeur();
  }
}
