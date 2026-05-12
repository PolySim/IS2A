import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/insert")
public class Insert extends HttpServlet {

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

      String SQLrequestMaxId = "select max(id) from etudiant;";
      stmt = connect.prepareStatement(SQLrequestMaxId);
      rs = stmt.executeQuery();
      if (!rs.next()) {
        throw new ServletException(
          "Aucun étudiant trouvé dans la base de données."
        );
      }
      int maxId = rs.getInt(1);

      String SQLrequestInsert =
        "insert into etudiant (id, nom, prenom) values (?, ?, ?);";
      if (
        req.getParameter("first_name") == null ||
        req.getParameter("last_name") == null
      ) {
        throw new ServletException(
          "Les paramètres first_name et last_name sont obligatoires."
        );
      }
      stmt = connect.prepareStatement(SQLrequestInsert);
      stmt.setInt(1, maxId + 1);
      stmt.setString(2, req.getParameter("first_name"));
      stmt.setString(3, req.getParameter("last_name"));
      stmt.executeUpdate();

      PrintWriter out = res.getWriter();
      out.println("<html><body>Insertion réussie.</body></html>");
    } catch (SQLException e) {
      throw new ServletException();
    }
  }
}
