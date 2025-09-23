import exception.ListVideException;
import liste.ListeQueue;

public class Main {
  public static void main(String[] args) {
    ListeQueue<Integer> liste = new ListeQueue<Integer>();

    liste.add(1);
    liste.add(2);
    liste.add(3);
    liste.add(4);
    liste.add(5);

    System.out.println(liste);

    try {
      liste.pop();
    } catch (ListVideException e) {
      System.out.println(e.getMessage());
    }

    System.out.println(liste);

    try {
      liste.popLast();
    } catch (ListVideException e) {
      System.out.println(e.getMessage());
    }

    System.out.println(liste);

    ListeQueue<Integer> liste2 = new ListeQueue<Integer>();
    liste2.add(6);
    liste2.add(7);
    liste2.add(8);
    liste2.add(9);
    liste2.add(10);

    liste.add(liste2);

    System.out.println(liste);
  }
}
