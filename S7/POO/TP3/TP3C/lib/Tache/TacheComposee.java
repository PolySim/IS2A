package lib.Tache;

public class TacheComposee extends Tache {
  private TacheSimple[] taches;

  public TacheComposee(String id, TacheSimple[] taches) {
    super(id, sum_durations(taches));
    this.setTaches(taches);
  }

  public static int sum_durations(TacheSimple[] taches) {
    int sum = 0;
    for (TacheSimple tache : taches) {
      sum += tache.getDuree();
    }
    return sum;
  }

  public void setTaches(TacheSimple[] taches) {
    this.taches = taches;
  }

  public TacheSimple[] getTaches() {
    return this.taches;
  }

  public int cout() {
    int sum = 0;
    for (TacheSimple tache : this.taches) {
      sum += tache.cout();
    }
    return sum;
  }

  public String toString() {
    return this.getId();
  }
}
