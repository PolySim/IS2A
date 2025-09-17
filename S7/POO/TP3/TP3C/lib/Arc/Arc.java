package lib.Arc;

import lib.Tache.Tache;

public class Arc {
  private Tache from;
  private Tache to;

  public Arc(Tache from, Tache to) {
    this.setFrom(from);
    this.setTo(to);
  }

  public void setFrom(Tache from) {
    this.from = from;
  }

  public void setTo(Tache to) {
    this.to = to;
  }
  
  public Tache getFrom() {
    return this.from;
  }

  public Tache getTo() {
    return this.to;
  }

  public String toString() {
    return this.getFrom().getId() + " -> " + this.getTo().getId();
  }
}
