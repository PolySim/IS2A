package lib.Tache;

public class TacheSimple extends Tache {
  private final int COUT;

  public TacheSimple(String id, int duree, int COUT) {
    super(id, duree);
    this.COUT = COUT;
  }

  public int cout() {
    return this.COUT;
  }

  public String toString() {
    return this.getId();
  }
}
