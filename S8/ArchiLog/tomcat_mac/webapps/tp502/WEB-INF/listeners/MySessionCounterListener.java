import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class MySessionCounterListener implements HttpSessionListener {

  int globalCounter = 0;

  public void sessionCreated(HttpSessionEvent se) {
    se.getSession().setMaxInactiveInterval(10);
    globalCounter++;
    System.out.println(
      "Nouvelle session - nombre de sessions : " + globalCounter
    );
  }

  public void sessionDestroyed(HttpSessionEvent se) {
    globalCounter--;
    System.out.println("Fin session - nombre de sessions : " + globalCounter);
  }
}
