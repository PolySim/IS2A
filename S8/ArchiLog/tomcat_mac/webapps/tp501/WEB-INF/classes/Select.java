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

      String SQLrequestSelect = "select * from etudiant;";
      stmt = connect.prepareStatement(SQLrequestSelect);
      rs = stmt.executeQuery();

      res.setContentType("text/html;charset=UTF-8");
      PrintWriter out = res.getWriter();
      out.println(
        "<html><head><link rel=\"stylesheet\" href=\"style.css\" /></head><body>"
      );

      out.println("<div class=\"table\">");
      while (rs.next()) {
        out.println(
          "<span>" +
            rs.getString("nom") +
            " " +
            rs.getString("prenom") +
            "</span>"
        );
      }
      out.println("</div>");
      out.println(new InsertHtml().toString());
      out.println("</body></html>");
    } catch (SQLException e) {
      throw new ServletException();
    }
  }
}
