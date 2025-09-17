package exceptions;

public class NomTropLongException extends NomVideException {
  public NomTropLongException(String message) {
    super(message);
  }

  public NomTropLongException() {
    this("Le nom est trop long !");
  }
}
