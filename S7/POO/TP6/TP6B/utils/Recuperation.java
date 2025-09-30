package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import carte.Carte;
import creation.Creation;
import plat.Plat;

public class Recuperation {
  private Carte carte;

  public Recuperation() {
    this.carte = recuperation();
  }

  private static Carte recuperation() {
    try {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(Creation.FILENAME));
      Carte carte = (Carte) in.readObject();
      in.close();
      return carte;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Plat getPlat(String nom) {
    return this.carte.getPlat(nom);
  }

}
