package elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Buchers {
  private ArrayList<Bucher> buchers;

  public Buchers() {
    this.buchers = new ArrayList<Bucher>();
  }

  public ArrayList<Bucher> getBuchers() {
    return this.buchers;
  }

  public Bucher getStart() {
    Bucher init = new Bucher(0, 0);
    int i = 0;
    while (i < this.getBuchers().size() && !this.getBuchers().get(i).equals(init)) {
      i++;
    }
    return this.getBuchers().get(i);
  }

  public void add(Bucher bucher) {
    this.getBuchers().add(bucher);
  }

  public boolean isConnexe() {
    Set<Bucher> isVisited = new HashSet<>();
    LinkedList<Bucher> next = new LinkedList<>();

    Bucher start = this.getStart();
    next.add(start);
    isVisited.add(start);

    while (!next.isEmpty()) {
      Bucher current = next.remove(0);

      for (Bucher voisin : current.getVoisins().getBuchers()) {
        if (!isVisited.contains(voisin)) {
          isVisited.add(voisin);
          next.add(voisin);
        }
      }
    }

    System.out.println(isVisited.size());
    System.out.println(this.getBuchers().size());
    return isVisited.size() == this.getBuchers().size();
  }

  public String toString() {
    String result = "";
    for (Bucher bucher : this.getBuchers()) {
      result += bucher.toString() + "\n";
    }
    return result;
  }

}
