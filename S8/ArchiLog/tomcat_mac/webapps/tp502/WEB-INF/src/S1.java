import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/s1")
public class S1 extends HttpServlet {

  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    /*
    res.sendRedirect("/tp502/s2");
    Requête créée pour l'URL : http://localhost:8080/tp502/s1
    Date : Wed May 13 17:36:31 CEST 2026
    /tp502/s1
    Temps de traitement : 227167 ns
    Requête créée pour l'URL : http://localhost:8080/tp502/s2
    Date : Wed May 13 17:36:31 CEST 2026
    /tp502/s2
    Temps de traitement : 96083 ns
    */
    req.getRequestDispatcher("/s2").forward(req, res);
    /*
    Requête créée pour l'URL : http://localhost:8080/tp502/s1
    Date : Wed May 13 17:39:17 CEST 2026
    /tp502/s1
    Temps de traitement : 5951917 ns
    */
  }
}
