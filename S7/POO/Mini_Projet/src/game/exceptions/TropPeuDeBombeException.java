package game.exceptions;

public class TropPeuDeBombeException extends Exception {
  public TropPeuDeBombeException(String message) {
    super(message);
  }

  public TropPeuDeBombeException() {
    this("Le nombre de bombes doit être supérieur à 0 !");
  }
}