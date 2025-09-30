import utils.CreationPlats;
import utils.Vente;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== Système de Gestion de Restaurant ===");
    System.out.println("1. Créer une nouvelle carte");
    System.out.println("2. Commencer une vente");
    System.out.print("Votre choix : ");

    int choix = scanner.nextInt();
    scanner.nextLine(); // Consommer le retour à la ligne

    switch (choix) {
      case 1:
        System.out.print("Nom du fichier pour sauvegarder la carte : ");
        String fichierCarte = scanner.nextLine();
        CreationPlats creation = new CreationPlats(fichierCarte);
        creation.creerCarte();
        break;

      case 2:
        System.out.print("Nom du fichier contenant la carte : ");
        String fichierCarteVente = scanner.nextLine();
        Vente vente = new Vente(fichierCarteVente);
        vente.commencerVente();
        break;

      default:
        System.out.println("Choix invalide !");
    }

    scanner.close();
  }
}
