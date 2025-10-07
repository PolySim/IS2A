package exceptions;

public class PasMajeurException extends Exception {

  public PasMajeurException(String message) {
    super(message);
  }

  public PasMajeurException() {
    super("L'étudiant n'est pas majeur");
  }

}
