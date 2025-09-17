package lib.Workflow;

import exceptions.ArcInvalideException;
import exceptions.CycleDetecteException;
import exceptions.TacheDejaExistanteException;
import exceptions.TacheInconnueException;
import lib.Arc.Arc;
import lib.Arc.Arcs;
import lib.Tache.Tache;
import lib.Tache.Taches;

public class Workflow {
  private Taches taches;
  private Arcs arcs;

  private Workflow(Taches taches, Arcs arcs) {
    this.setTaches(new Taches());
    this.setArcs(arcs);
  }
  
  public Workflow() {
    this(new Taches(), new Arcs());
  }

  public Taches getTaches() {
    return this.taches;
  }

  public void setTaches(Taches taches) {
    this.taches = taches;
  }

  public void ajouterTache(Tache tache) throws TacheDejaExistanteException {
    this.getTaches().ajouterTache(tache);
  }

  
  public void setArcs(Arcs arcs) {
    this.arcs = arcs;
  }
  
  public Arcs getArcs() {
    return this.arcs;
  }
  
  public void ajouterArc(Arc arc) throws TacheInconnueException, ArcInvalideException, CycleDetecteException {
    if (this.getTaches().getTacheIndex(arc.getFrom()) == -1 || this.getTaches().getTacheIndex(arc.getTo()) == -1) {
      throw new TacheInconnueException();
    }
    this.getArcs().ajouterArc(arc);
  }

  public String toString() {
    String result = "";

    result += "\nTaches :\n";
    result += this.getTaches().toString();

    result += "\nArcs :\n";
    result += this.getArcs().toString();
    return result;
  }
}
