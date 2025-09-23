package exception;

public class ListVideException extends Exception {
  public ListVideException(String message) {
    super(message);
  }

  public ListVideException() {
    this("La liste est vide !");
  }
  
}
