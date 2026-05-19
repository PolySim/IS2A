import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Date;

@WebFilter(urlPatterns = { "/*" })
public class Logs extends HttpFilter {

  public void doFilter(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain next
  ) throws ServletException, IOException {
    long startTime = System.nanoTime();
    System.out.println("Date : " + new Date());
    System.out.println(req.getRequestURI());

    try {
      next.doFilter(req, res);
    } finally {
      long durationNs = (System.nanoTime() - startTime);
      System.out.println("Temps de traitement : " + durationNs + " ns");
    }
  }
}
