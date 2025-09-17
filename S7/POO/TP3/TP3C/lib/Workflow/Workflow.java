package lib.Workflow;

import exceptions.ArcInvalideException;
import exceptions.CycleDetecteException;
import exceptions.TacheInconnueException;
import lib.Arc.Arc;
import lib.Tache.Tache;
import lib.Tache.Taches;

public class Workflow {
  private Taches taches;
  private Arc[] arcs;

  private Workflow(Taches taches, Arc[] arcs) {
    this.setTaches(new Taches());
    this.setArcs(arcs);
  }
  
  public Workflow() {
    this(new Taches(), new Arc[100]);
  }

  public Taches getTaches() {
    return this.taches;
  }

  public void setTaches(Taches taches) {
    this.taches = taches;
  }


  public void setArcs(Arc[] arcs) {
    this.arcs = arcs;
  }

  public Arc[] getArcs() {
    return this.arcs;
  }

  public void replaceArc(Arc a, int index) {
    this.arcs[index] = a;
  }

  public int getLastArcIndex() {
    int i = 0;
    while (i < this.arcs.length && this.getArcs()[i] != null) {
      i++;
    }
    return i;
  }

  public void controleArc(Arc a) throws TacheInconnueException, ArcInvalideException, CycleDetecteException {
    if (this.getTaches().getTacheIndex(a.getFrom()) == -1 || this.getTaches().getTacheIndex(a.getTo()) == -1) {
      throw new TacheInconnueException();
    }
    if (a.getFrom().getId().equals(a.getTo().getId())) {
      throw new ArcInvalideException();
    }
    if (this.cycleExiste(a)) {
      throw new CycleDetecteException();
    }
  }

  public Arc[] getParents(Tache t) {
    Arc[] parents = new Arc[this.getLastArcIndex()];
    for (int i = 0; i < this.getLastArcIndex(); i++) {
      if (this.getArcs()[i].getTo().getId().equals(t.getId())) {
        parents[i] = this.getArcs()[i];
      }
    }
    return parents;
  }

  public boolean cycleExiste(Arc a, Tache to) {
    Arc[] parents = this.getParents(a.getFrom());
    for (Arc parent : parents) {
      if (parent == null) {
        continue;
      }
    }
    if (parents.length == 0) {
      return false;
    }
    for (int i = 0; i < parents.length; i++) {
      if (parents[i] == null) {
        continue;
      }
      if (parents[i].getFrom().getId().equals(to.getId())) {
        return true;
      }
    }

    boolean result = false;
    int i = 0;
    while (!result && i < parents.length) {
      if (parents[i] == null) {
        i++;
        continue;
      }
      result = this.cycleExiste(parents[i], to);
      i++;
    }
    return result;
  }

  public boolean cycleExiste(Arc a) {
    return this.cycleExiste(a, a.getTo());
  }

  private int getArcIndex(Arc a) {
    for (int i = 0; i < this.getLastArcIndex(); i++) {
      if (this.getArcs()[i].getFrom().getId().equals(a.getFrom().getId()) && this.getArcs()[i].getTo().getId().equals(a.getTo().getId())) {
        return i;
      }
    }
    return -1;
  }

  public void ajouterArc(Arc a) throws TacheInconnueException, ArcInvalideException, CycleDetecteException {
    this.controleArc(a);
    Arc[] parents = this.getParents(a.getTo());
    int index = 0;
    for (int i = 0; i < parents.length; i++) {
      if (parents[i] == null) {
        continue;
      }
      if (parents[i + 1] == null) {
        index = this.getArcIndex(parents[i]);
      }
    }
    this.insertArc(a, index);
  }

  private void insertArc(Arc a, int index) {
    for (int i = this.getLastArcIndex() ; i > index ; i--) {
      this.replaceArc(this.getArcs()[i - 1], i);
    }
    this.replaceArc(a, index);
  }

  public String toString() {
    String result = "";

    result += "\nTaches :\n";
    result += this.getTaches().toString();

    result += "\nArcs :\n";
    for (int i = 0; i < this.getLastArcIndex(); i++) {
      result += this.getArcs()[i].getFrom().getId() + " -> " + this.getArcs()[i].getTo().getId() + " ";
      result += "\n";
    }
    return result;
  }
}
