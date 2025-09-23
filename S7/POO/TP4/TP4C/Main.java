import element.Person;
import liste.ListeSequence;

public class Main {
  public static void main(String[] args) {
    ListeSequence<Integer> listeInteger = new ListeSequence<Integer>();

    listeInteger.add(3);
    listeInteger.add(1);
    listeInteger.add(4);
    listeInteger.add(2);
    listeInteger.add(5);

    System.out.println("ListeInteger");
    System.out.println(listeInteger);
    System.out.println(listeInteger.getSubSequence());

    ListeSequence<String> listeString = new ListeSequence<String>();
    listeString.add("a");
    listeString.add("e");
    listeString.add("c");
    listeString.add("b");
    listeString.add("d");
    
    System.out.println("ListeString");
    System.out.println(listeString);
    System.out.println(listeString.getSubSequence());

    ListeSequence<Person> listePerson = new ListeSequence<Person>();
    listePerson.add(new Person(3));
    listePerson.add(new Person(1));
    listePerson.add(new Person(4));
    listePerson.add(new Person(2));
    listePerson.add(new Person(5));

    System.out.println("ListePerson");
    System.out.println(listePerson);
    System.out.println(listePerson.getSubSequence());
  }
}
