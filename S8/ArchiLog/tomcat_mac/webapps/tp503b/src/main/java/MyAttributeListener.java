import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.util.*;

@WebListener
public class MyAttributeListener implements HttpSessionAttributeListener {

  public void attributeAdded(HttpSessionBindingEvent se) {
    System.out.println("Attribu ajouté: " + se.getName());
  }

  public void attributeRemoved(HttpSessionBindingEvent se) {
    System.out.println("Attribu supprimé: " + se.getName());
  }

  public void attributeReplaced(HttpSessionBindingEvent se) {
    System.out.println("Attribu remplacé: " + se.getName());
  }
}
