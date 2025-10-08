package game.exceptions;

public class TropPetiteGridException extends Exception {
  public TropPetiteGridException(String message) {
    super(message);
  }

  public TropPetiteGridException() {
    this("La taille de la grille doit être supérieure à 3 !");
  }

}
