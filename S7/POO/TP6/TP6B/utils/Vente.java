package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

import carte.Carte;
import carte.Menu;
import plat.Accompagnement;
import plat.Boisson;
import plat.Plat;
import plat.PlatPrincipal;
import user.Acheteur;

public class Vente {
  private String carteFilename;
  private Scanner scanner;
  private Carte carte;

  public Vente(String carteFilename) {
    this.carteFilename = carteFilename;
    this.scanner = new Scanner(System.in);
    this.carte = chargerCarte();
  }

  public void commencerVente() {
    if (carte == null) {
      System.out.println("Impossible de charger la carte. Vente annulée.");
      return;
    }

    System.out.print("Nom de l'acheteur : ");
    String nomAcheteur = scanner.nextLine();
    Acheteur acheteur = new Acheteur(nomAcheteur);

    System.out.println("=== Bienvenue " + nomAcheteur + " ! ===");
    System.out.println("\nCarte disponible :");
    System.out.println(carte.toString());

    boolean continuer = true;
    while (continuer) {
      System.out.println("\nQue voulez-vous commander ?");
      System.out.println("1. Commander un plat individuel");
      System.out.println("2. Commander un menu complet");
      System.out.println("3. Voir votre panier");
      System.out.println("4. Terminer la commande");
      System.out.print("Votre choix : ");

      int choix = scanner.nextInt();
      scanner.nextLine();

      switch (choix) {
        case 1:
          commanderPlatIndividuel(acheteur);
          break;
        case 2:
          commanderMenu(acheteur);
          break;
        case 3:
          afficherPanier(acheteur);
          break;
        case 4:
          continuer = false;
          break;
        default:
          System.out.println("Choix invalide !");
      }
    }

    String fichierAddition = "addition_" + nomAcheteur + ".txt";
    acheteur.writeAddition(fichierAddition);
    System.out.println("Addition sauvegardée dans " + fichierAddition);
    System.out.println("Merci pour votre commande !");
  }

  private void commanderPlatIndividuel(Acheteur acheteur) {
    System.out.print("Nom du plat à commander : ");
    String nomPlat = scanner.nextLine();

    Plat plat = carte.getPlat(nomPlat);
    if (plat != null) {
      acheteur.acheter(plat);
      System.out.println("Plat ajouté au panier : " + plat.toString());
    } else {
      System.out.println("Plat non trouvé dans la carte !");
    }
  }

  private void commanderMenu(Acheteur acheteur) {
    System.out.println("=== Commande d'un menu ===");

    System.out.print("Nom du plat principal : ");
    String nomPlat = scanner.nextLine();
    Plat platPrincipal = carte.getPlat(nomPlat);

    System.out.print("Nom de l'accompagnement : ");
    String nomAccomp = scanner.nextLine();
    Plat accompagnement = carte.getPlat(nomAccomp);

    System.out.print("Nom de la boisson : ");
    String nomBoisson = scanner.nextLine();
    Plat boisson = carte.getPlat(nomBoisson);

    if (platPrincipal instanceof PlatPrincipal &&
        accompagnement instanceof Accompagnement &&
        boisson instanceof Boisson) {

      Menu menu = new Menu((Boisson) boisson, (PlatPrincipal) platPrincipal, (Accompagnement) accompagnement);
      acheteur.acheter(menu);
      System.out.println("Menu ajouté au panier !");
      System.out.println("Prix du menu avec remise : " + menu.getPrix() + "€");
    } else {
      System.out.println("Erreur : vérifiez que vous avez sélectionné les bons types de plats pour le menu.");
    }
  }

  private void afficherPanier(Acheteur acheteur) {
    System.out.println("=== Panier de " + acheteur.getNom() + " ===");
    if (acheteur.getPlats().isEmpty()) {
      System.out.println("Panier vide");
    } else {
      double total = 0;
      for (int i = 0; i < acheteur.getPlats().size(); i++) {
        System.out.println((i + 1) + ". " + acheteur.getPlats().get(i).toString());
        total += acheteur.getPlats().get(i).getPrix();
      }
      System.out.println("Total : " + total + "€");
    }
  }

  private Carte chargerCarte() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(carteFilename))) {
      return (Carte) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Erreur lors du chargement de la carte : " + e.getMessage());
      return null;
    }
  }

}
