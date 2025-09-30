package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import carte.Carte;
import carte.Menu;
import plat.Accompagnement;
import plat.Boisson;
import plat.PlatPrincipal;

public class CreationPlats {
  private String filename;
  private Scanner scanner;

  public CreationPlats(String filename) {
    this.filename = filename;
    this.scanner = new Scanner(System.in);
  }

  public void creerCarte() {
    Carte carte = new Carte();

    System.out.println("=== Création interactive d'une carte ===");

    boolean continuer = true;
    while (continuer) {
      System.out.println("\nQue voulez-vous ajouter ?");
      System.out.println("1. Plat principal");
      System.out.println("2. Accompagnement");
      System.out.println("3. Boisson");
      System.out.println("4. Menu complet");
      System.out.println("5. Terminer et sauvegarder");
      System.out.print("Votre choix : ");

      int choix = scanner.nextInt();
      scanner.nextLine();

      switch (choix) {
        case 1:
          this.ajouterPlatPrincipal(carte);
          break;
        case 2:
          this.ajouterAccompagnement(carte);
          break;
        case 3:
          this.ajouterBoisson(carte);
          break;
        case 4:
          this.ajouterMenu(carte);
          break;
        case 5:
          continuer = false;
          break;
        default:
          System.out.println("Choix invalide !");
      }
    }

    this.sauvegarderCarte(carte);
    System.out.println("Carte sauvegardée dans " + filename);
  }

  private void ajouterPlatPrincipal(Carte carte) {
    System.out.print("Nom du plat principal : ");
    String nom = scanner.nextLine();
    System.out.print("Prix : ");
    double prix = scanner.nextDouble();
    scanner.nextLine();

    PlatPrincipal plat = new PlatPrincipal(nom, prix);
    carte.add(plat);
    System.out.println("Plat principal ajouté !");
  }

  private void ajouterAccompagnement(Carte carte) {
    System.out.print("Nom de l'accompagnement : ");
    String nom = scanner.nextLine();
    System.out.print("Prix : ");
    double prix = scanner.nextDouble();
    scanner.nextLine();

    Accompagnement accompagnement = new Accompagnement(nom, prix);
    carte.add(accompagnement);
    System.out.println("Accompagnement ajouté !");
  }

  private void ajouterBoisson(Carte carte) {
    System.out.print("Nom de la boisson : ");
    String nom = scanner.nextLine();
    System.out.print("Prix : ");
    double prix = scanner.nextDouble();
    scanner.nextLine();

    Boisson boisson = new Boisson(nom, prix);
    carte.add(boisson);
    System.out.println("Boisson ajoutée !");
  }

  private void ajouterMenu(Carte carte) {
    System.out.println("=== Création d'un menu ===");

    System.out.print("Nom du plat principal : ");
    String nomPlat = scanner.nextLine();
    System.out.print("Prix du plat principal : ");
    double prixPlat = scanner.nextDouble();
    scanner.nextLine();

    System.out.print("Nom de l'accompagnement : ");
    String nomAccomp = scanner.nextLine();
    System.out.print("Prix de l'accompagnement : ");
    double prixAccomp = scanner.nextDouble();
    scanner.nextLine();

    System.out.print("Nom de la boisson : ");
    String nomBoisson = scanner.nextLine();
    System.out.print("Prix de la boisson : ");
    double prixBoisson = scanner.nextDouble();
    scanner.nextLine();

    PlatPrincipal platPrincipal = new PlatPrincipal(nomPlat, prixPlat);
    Accompagnement accompagnement = new Accompagnement(nomAccomp, prixAccomp);
    Boisson boisson = new Boisson(nomBoisson, prixBoisson);

    carte.add(platPrincipal);
    carte.add(accompagnement);
    carte.add(boisson);

    System.out.println("Menu créé et plats ajoutés à la carte !");
    System.out.println("Prix total sans remise : " + (prixPlat + prixAccomp + prixBoisson) + "€");
    Menu menu = new Menu(boisson, platPrincipal, accompagnement);
    System.out.println("Prix du menu avec remise de 30% : " + menu.getPrix() + "€");
  }

  private void sauvegarderCarte(Carte carte) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
      oos.writeObject(carte);
    } catch (IOException e) {
      System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
    }
  }
}
