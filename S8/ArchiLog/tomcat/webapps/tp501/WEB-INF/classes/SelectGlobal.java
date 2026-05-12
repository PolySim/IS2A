import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/select")
public class Select extends HttpServlet {

  private Connection connect;
  private PreparedStatement stmt;
  private ResultSet rs;

  String url = "jdbc:postgresql://serveur-etu.polytech-lille.fr/sdesdevi_db";
  String user = "sdesdevi";
  String passwd = "postgres";

  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    try {
      connect = DriverManager.getConnection(url, user, passwd);
    } catch (SQLException e) {
      throw new ServletException();
    }
  }
}
