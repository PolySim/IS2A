package exceptions;

public class ProblemeCategorieException extends Exception {
  public ProblemeCategorieException(String message) {
    super(message);
  }

  public ProblemeCategorieException() {
    this("La catégorie est invalide !");
  }
}
