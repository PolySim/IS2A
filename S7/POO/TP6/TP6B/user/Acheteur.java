package user;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import plat.Achetable;

public class Acheteur {
  private String nom;
  private List<Achetable> plats;

  public Acheteur(String nom) {
    this.nom = nom;
    this.plats = new ArrayList<>();
  }

  public String getNom() {
    return this.nom;
  }

  public List<Achetable> getPlats() {
    return this.plats;
  }

  public void acheter(Achetable plat) {
    this.getPlats().add(plat);
  }

  public void writeAddition(String nomDuFichier) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(nomDuFichier))) {

      if (this.plats.isEmpty()) {
        writer.println("Aucun achat effectué.");
      } else {
        writer.println("Détail de la commande :");
        writer.println("----------------------------------------");

        double total = 0.0;
        int numeroItem = 1;

        for (Achetable item : this.plats) {
          writer.printf("%d. %-25s %8.2f€%n", numeroItem, getItemDescription(item), item.getPrix());
          total += item.getPrix();
          numeroItem++;
        }

        writer.println("----------------------------------------");
        writer.printf("TOTAL :                    %8.2f€%n", total);
      }

    } catch (IOException e) {
      System.err.println("Erreur lors de l'écriture de l'addition : " + e.getMessage());
    }
  }

  private String getItemDescription(Achetable item) {
    if (item.toString().contains("Menu")) {
      return "Menu complet";
    } else {
      return item.toString();
    }
  }
}
