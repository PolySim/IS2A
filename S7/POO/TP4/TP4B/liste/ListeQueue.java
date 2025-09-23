package liste;

import element.Maillon;
import exception.ListVideException;

public class ListeQueue<T> {
  private Maillon<T> first;

  public ListeQueue() {
    this.first = null;
  }

  public Maillon<T> getFirst() {
    return this.first;
  }

  public void setFirst(Maillon<T> first) {
    this.first = first;
  }

  public Maillon<T> getLast(Maillon<T> maillon) {
    if (maillon.getQueue() == null) {
      return maillon;
    }
    return this.getLast(maillon.getQueue());
  }

  public Maillon<T> getLast() {
    return this.getLast(this.getFirst());
  }

  private void controleListeVide() throws ListVideException {
    if (this.getFirst() == null) {
      throw new ListVideException();
    }
  }

  public Maillon<T> pop() throws ListVideException {
    this.controleListeVide();
    Maillon<T> result = this.getFirst();
    this.setFirst(this.getFirst().getQueue());
    return result;
  }

  public Maillon<T> popLast(Maillon<T> maillon) {
    if (maillon.getQueue().getQueue() == null) {
      Maillon<T> result = maillon.getQueue();
      maillon.setQueue(null);
      return result;
    }
    return this.popLast(maillon.getQueue());
  }

  public void add(T valeur) {
    Maillon<T> newMaillon = new Maillon<T>(valeur, first);
    this.setFirst(newMaillon);
  }

  public void add(ListeQueue<T> liste) {
    liste.getLast().setQueue(this.getFirst());
    this.setFirst(liste.getFirst());
  }

  public Maillon<T> popLast() throws ListVideException {
    this.controleListeVide();
    return this.popLast(this.getFirst());
  }

  public String toString() {
    Maillon<T> current = this.getFirst();
    String result = "";
    while (current != null) {
      result += current.toString() + " ";
      current = current.getQueue();
    }
    return result;
  }
}
