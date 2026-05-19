import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.util.*;

@WebListener
public class MyRequestListener implements ServletRequestListener {

  public void requestInitialized(ServletRequestEvent sre) {
    if (sre.getServletRequest() instanceof HttpServletRequest request) {
      System.out.println(
        "Requête créée pour l'URL : " + request.getRequestURL()
      );
      Map<String, String[]> parameters = request.getParameterMap();
      if (!parameters.isEmpty()) {
        System.out.println("Paramètres :");
        for (String key : parameters.keySet()) {
          System.out.println("\t" + key + " : " + parameters.get(key));
        }
      }
    }
  }
}
