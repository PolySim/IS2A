import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/select")
public class Select extends HttpServlet {

  String url = "jdbc:postgresql://localhost:5432/archilog";
  String user = "postgres";
  String passwd = "postgres";

  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    try (
      Connection connect = DriverManager.getConnection(url, user, passwd);
      PreparedStatement stmt = connect.prepareStatement("select * from etudiant;");
      ResultSet rs = stmt.executeQuery()
    ) {
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
      out.println("</body></html>");
    } catch (SQLException e) {
      throw new ServletException("Erreur SQL dans Select", e);
    }
  }
}
