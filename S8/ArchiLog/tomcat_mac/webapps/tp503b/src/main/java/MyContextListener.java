import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class MyContextListener implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(
    MyContextListener.class.getName()
  );

  String url = "jdbc:postgresql://localhost:5432/archilog";
  String user = "postgres";
  String passwd = "postgres";

  public void contextInitialized(ServletContextEvent sce) {
    try {
      Class.forName("org.postgresql.Driver");
      Connection connect = DriverManager.getConnection(url, user, passwd);
      ServletContext context = sce.getServletContext();
      context.setAttribute("connect", connect);
      System.out.println("Connexion BDD établie avec succès");
    } catch (ClassNotFoundException e) {
      LOGGER.log(
        Level.SEVERE,
        "Driver PostgreSQL introuvable. L'application démarre sans connexion BDD.",
        e
      );
    } catch (SQLException e) {
      LOGGER.log(
        Level.SEVERE,
        "Erreur de connexion à la base de données. L'application démarre sans connexion BDD.",
        e
      );
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    Connection connect = (Connection) context.getAttribute("connect");
    if (connect != null) {
      try {
        connect.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
