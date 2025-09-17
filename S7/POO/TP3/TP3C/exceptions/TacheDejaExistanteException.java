package exceptions;

public class TacheDejaExistanteException extends Exception {
  public TacheDejaExistanteException(String message) {
    super(message);
  }

  public TacheDejaExistanteException() {
    this("La tache existe déjà");
  }
}
