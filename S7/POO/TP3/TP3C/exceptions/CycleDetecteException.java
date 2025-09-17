package exceptions;

public class CycleDetecteException extends Exception {
  public CycleDetecteException(String message) {
    super(message);
  }

  public CycleDetecteException() {
    this("Le cycle est détecté");
  }
  
}
