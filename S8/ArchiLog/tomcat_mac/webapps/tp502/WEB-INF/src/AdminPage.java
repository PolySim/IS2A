import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/admin/page")
public class AdminPage extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<html><body><p>Mot de passe encodé");
    out.println(req.getAttribute("encrypt").toString());
    out.println("</p></body></html>");
  }
}
