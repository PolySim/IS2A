import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/compteur")
public class Compteur extends HttpServlet {

  int globalCount = 1;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    HttpSession session = req.getSession(true);
    Integer currentCount = (Integer) session.getAttribute("currentCount");
    if (currentCount == null) {
      currentCount = 0;
    }
    currentCount++;
    session.setAttribute("currentCount", currentCount);

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<html><body><p>Vous avez accédé");
    out.println(currentCount.intValue());
    out.println("fois à cette page sur les");
    out.println(globalCount);
    out.println("accès au total.");
    globalCount++;
    out.println("</p></body></html>");
  }
}
