package lib.Workflow;

import exceptions.ArcInvalideException;
import exceptions.CycleDetecteException;
import exceptions.TacheDejaExistanteException;
import exceptions.TacheInconnueException;
import lib.Arc.Arc;
import lib.Tache.Tache;

public class Workflow {
  private Tache[] taches;
  private Arc[] arcs;

  public Workflow(Tache[] taches, Arc[] arcs) {
    this.setTaches(taches);
    this.setArcs(arcs);
  }
  
  public Workflow() {
    this(new Tache[100], new Arc[100]);
  }

  public void setTaches(Tache[] taches) {
    this.taches = taches;
  }

  public Tache[] getTaches() {
    return this.taches;
  }

  public void replaceTache(Tache t, int index) {
    this.taches[index] = t;
  }

  public int getTacheIndex(Tache t) {
    int i = 0;
    while (i < this.getLastTacheIndex() && !this.getTaches()[i].getId().equals(t.getId())) {
      i++;
    }
    if (i == this.getLastTacheIndex()) {
      return -1;
    }
    return i;
  }

  public void controleTache(Tache t) throws TacheDejaExistanteException {
    int i = 0;
    boolean found = false;
    while (i < this.getLastTacheIndex() && !found) {
      if (this.taches[i].getId().equals(t.getId())) {
        found = true;
      }
      i++;
    }
    if (found) {
      throw new TacheDejaExistanteException();
    }
  }

  public int getLastTacheIndex() {
    int i = 0;
    while (i < this.taches.length && this.getTaches()[i] != null) {
      i++;
    }
    return i;
  }

  public void ajouterTache(Tache t) throws TacheDejaExistanteException {
    this.controleTache(t);
    this.replaceTache(t, this.getLastTacheIndex());
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
    if (this.getTacheIndex(a.getFrom()) == -1 || this.getTacheIndex(a.getTo()) == -1) {
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
    for (int i = 0; i < this.getLastTacheIndex(); i++) {
      result += this.getTaches()[i].getId() + " ";
    }
    result += "\n";

    result += "\nArcs :\n";
    for (int i = 0; i < this.getLastArcIndex(); i++) {
      result += this.getArcs()[i].getFrom().getId() + " -> " + this.getArcs()[i].getTo().getId() + " ";
      result += "\n";
    }
    return result;
  }
}
