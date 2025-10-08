package game.exceptions;

public class TropDeBombeException extends Exception {
  public TropDeBombeException(String message) {
    super(message);
  }

  public TropDeBombeException() {
    this("Le nombre de bombes est trop grand !");
  }
}
