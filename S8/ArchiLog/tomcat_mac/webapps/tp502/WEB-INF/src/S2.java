import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/s2")
public class S2 extends HttpServlet {

  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("text/html;charset=UTF-8");
    HttpSession session = req.getSession(true);
    PrintWriter out = res.getWriter();
    out.println("<h1>On est sur le S2</h1>");
  }
}
