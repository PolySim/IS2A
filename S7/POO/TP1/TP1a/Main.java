import media.Auteur;
import media.Livre;
import mediatheque.BibliothequeAdd;

public class Main {
  static Livre[] biblio = {
    new Livre("Nana", "Emile Zola", 10.0),
    new Livre("Germinal", "Emile Zola", 12.0),
    new Livre("Claude␣Gueux", new Auteur("Hugo"),5.0),
    new Livre("Les Misérables", new Auteur("Victor Hugo"), 15.0),
  };

  public static void main(String[] args) {
    BibliothequeAdd bibliotheque = new BibliothequeAdd();
    for (Livre livre : biblio) {
      bibliotheque.addLivre(livre);
    }
    System.out.println(bibliotheque.show());
    bibliotheque.removeLivre(1);
    System.out.println(bibliotheque.show());
  } 
}
