package liste;

import java.util.ArrayList;

public class ListeSequence<T extends Comparable<T>> {
  private ArrayList<T> liste;

  public ListeSequence(ArrayList<T> liste) {
    this.setListe(liste);
  }

  public ListeSequence() {
    this.setListe(new ArrayList<T>());
  }
  
  public ArrayList<T> getListe() {
    return liste;
  }

  public void setListe(ArrayList<T> liste) {
    this.liste = liste;
  }

  public void replace(int index, T element) {
    this.getListe().set(index, element);
  }

  public void add(T element) {
    this.getListe().add(element);
  }

  public void remove(int index) {
    this.getListe().remove(index);
  }

  private int getPreviousIndex(ArrayList<Integer> liste, int index) {
    int max = -1;
    for (int i = index - 1; i >= 0; i--) {
      if (this.getListe().get(index).compareTo(this.getListe().get(i)) > 0 && liste.get(i) > max) {
        max = liste.get(i);
      }
    }
    return max;
  }

  private ArrayList<T> generateSubSequence(ArrayList<Integer> result, int maxIndex) {
    ArrayList<T> subSequence = new ArrayList<T>();
    int currentIndex = maxIndex;
    while (result.get(currentIndex) != 0) {

      subSequence.add(this.getListe().get(currentIndex));
      int i = currentIndex - 1;
      while (this.getListe().get(i).compareTo(this.getListe().get(currentIndex)) > 0 && i > 0) {
        i--;
      }
      currentIndex = i;
    }

    subSequence.add(this.getListe().get(currentIndex));
    return subSequence;
  }

  public ArrayList<T> getSubSequence() {
    ArrayList<Integer> result = new ArrayList<Integer>();
    int maxIndex = 0;
    int max = 0;
    for (int i = 0; i < this.getListe().size(); i++) {
      int previousIndex = this.getPreviousIndex(result, i);
      result.add(previousIndex + 1);
      if (previousIndex + 1 > max) {
        max = previousIndex + 1;
        maxIndex = i;
      }
    }

    System.out.println(result);

    return this.generateSubSequence(result, maxIndex);
  }

  public String toString() {
    String result = "";
    for (T element : this.getListe()) {
      result += element + " ";
    }
    return result;
  }
}
