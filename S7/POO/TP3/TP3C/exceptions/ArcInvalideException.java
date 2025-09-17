package exceptions;

public class ArcInvalideException extends Exception {
  public ArcInvalideException(String message) {
    super(message);
  }

  public ArcInvalideException() {
    this("L'arc est invalide");
  }
  
}
