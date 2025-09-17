package exceptions;

public class TacheInconnueException extends Exception {
  public TacheInconnueException(String message) {
    super(message);
  }

  public TacheInconnueException() {
    this("La tache est inconnue");
  }
}
