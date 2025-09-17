package lib.Arc;

import exceptions.ArcInvalideException;
import exceptions.CycleDetecteException;
import exceptions.TacheInconnueException;
import lib.Tache.Tache;

public class Arcs {
  private Arc[] arcs;
  private int last;

  public Arcs() {
    this.arcs = new Arc[100];
    this.setLast(0);
  }

  public void setLast(int last) {
    this.last = last;
  }

  public int getLast() {
    return this.last;
  }

  private void incLast() {
    this.setLast(this.getLast() + 1);
  }

  public Arc[] getArcs() {
    return this.arcs;
  }

  private void replace(Arc a, int index) {
    this.arcs[index] = a;
  }

  public void insert(Arc a, int index) {
    for (int i = this.getLast() ; i > index ; i--) {
      this.replace(this.getArcs()[i - 1], i);
    }
    this.replace(a, index);
    this.incLast();
  }

  public int getArcIndex(Arc a) {
    for (int i = 0; i < this.getLast(); i++) {
      if (this.getArcs()[i].getFrom().getId().equals(a.getFrom().getId()) && this.getArcs()[i].getTo().getId().equals(a.getTo().getId())) {
        return i;
      }
    }
    return -1;
  }

  public Arcs getParents(Tache t) {
    Arcs parents = new Arcs();
    for (int i = 0; i < this.getLast(); i++) {
      if (this.getArcs()[i].getTo().getId().equals(t.getId())) {
        parents.insert(this.getArcs()[i], parents.getLast());
      }
    }
    return parents;
  }

  public boolean cycleExiste(Arc a, Tache to) {
    Arcs parents = this.getParents(a.getFrom());
    if (parents.getLast() == 0) {
      return false;
    }
    for (int i = 0; i < parents.getLast(); i++) {
      if (parents.getArcs()[i].getFrom().getId().equals(to.getId())) {
        return true;
      }
    }

    boolean result = false;
    int i = 0;
    while (!result && i < parents.getLast()) {
      if (parents.getArcs()[i] == null) {
        i++;
        continue;
      }
      result = this.cycleExiste(parents.getArcs()[i], to);
      i++;
    }
    return result;
  }

  public boolean cycleExiste(Arc a) {
    return this.cycleExiste(a, a.getTo());
  }

  private void controleArc(Arc a) throws TacheInconnueException, ArcInvalideException, CycleDetecteException {
    if (a.getFrom().getId().equals(a.getTo().getId())) {
      throw new ArcInvalideException();
    }
    if (this.cycleExiste(a)) {
      throw new CycleDetecteException();
    }
  }

  public void ajouterArc(Arc a) throws TacheInconnueException, ArcInvalideException, CycleDetecteException {
    this.controleArc(a);
    Arcs parents = this.getParents(a.getTo());
    int index = 0;
    for (int i = 0; i < parents.getLast(); i++) {
      if (parents.getArcs()[i + 1] == null) {
        index = this.getArcIndex(parents.getArcs()[i]);
      }
    }
    this.insert(a, index);
  }

  public String toString() {
    String result = "";
    for (int i = 0; i < this.getLast(); i++) {
      result += this.getArcs()[i].toString() + "\n";
    }
    return result;
  }
  
}
