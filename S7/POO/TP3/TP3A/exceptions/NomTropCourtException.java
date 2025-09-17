package exceptions;

public class NomTropCourtException extends Exception {

  public NomTropCourtException(String message) {
    super(message);
  }

  public NomTropCourtException() {
    this("Le nom de l'auteur est trop court !");
  }
}
