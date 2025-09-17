package lib.Tache;

import exceptions.TacheDejaExistanteException;

public class Taches {
  private Tache[] taches;
  private int last;

  public Taches() {
    this.taches = new Tache[100];
    this.setLast(0);
  }

  private void setLast(int last) {
    this.last = last;
  }

  public int getLast() {
    return this.last;
  }

  private void incLast() {
    this.setLast(this.getLast() + 1);
  }

  public Tache[] getTaches() {
    return this.taches;
  }

  private void replace(Tache t, int index) {
    this.taches[index] = t;
  }

  public void insert(Tache t, int index) {
    for (int i = this.getLast() ; i > index ; i--) {
      this.replace(this.getTaches()[i - 1], i);
    }
    this.replace(t, index);
    this.incLast();
  }

  public int getTacheIndex(Tache t) {
    int i = 0;
    while (i < this.getLast() && !this.getTaches()[i].getId().equals(t.getId())) {
      i++;
    }
    if (i == this.getLast()) {
      return -1;
    }
    return i;
  }

  private void controleTache(Tache t) throws TacheDejaExistanteException {
    if (this.getTacheIndex(t) != -1) {
      throw new TacheDejaExistanteException();
    }
  }

  public void ajouterTache(Tache t) throws TacheDejaExistanteException {
    this.controleTache(t);
    this.insert(t, this.getLast());
  }

  public String toString() {
    String result = "";
    for (int i = 0; i < this.getLast(); i++) {
      result += this.getTaches()[i].toString() + "\n";
    }
    return result;
  }
}
