import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.*;

@WebListener
public class MyContextListener implements ServletContextListener {

  String url = "jdbc:postgresql://localhost:5432/archilog";
  String user = "postgres";
  String passwd = "postgres";

  public void contextInitialized(ServletContextEvent sce) {
    try {
      Connection connect = DriverManager.getConnection(url, user, passwd);
      ServletContext context = sce.getServletContext();
      context.setAttribute("connect", connect);
    } catch (SQLException e) {
      e.printStackTrace();
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
