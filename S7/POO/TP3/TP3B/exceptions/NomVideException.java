package exceptions;

public class NomVideException extends Exception {
  public NomVideException(String message) {
    super(message);
  }

  public NomVideException() {
    this("Le nom est vide !");
  }
  
}
