package lib.Tache;

public abstract class Tache {
  private String id;
  private int duree;

  public Tache(String id, int duree) {
    this.setId(id);
    this.setDuree(duree);
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public void setDuree(int duree) {
    this.duree = duree;
  }

  public String getId() {
    return this.id;
  }

  public int getDuree() {
    return this.duree;
  }

  public abstract int cout();
}
