import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Date;

@WebFilter(urlPatterns = { "/test" })
public class LogsTest extends HttpFilter {

  public void doFilter(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain next
  ) throws ServletException, IOException {
    System.out.println("Only for /test");
    System.out.println("Date : " + new Date());
    System.out.println(req.getRequestURI());

    next.doFilter(req, res);
  }
}
