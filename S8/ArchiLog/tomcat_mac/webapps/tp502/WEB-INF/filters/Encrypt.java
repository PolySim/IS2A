import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter(urlPatterns = { "/admin/*" })
public class Encrypt extends HttpFilter {

  private String encrypt(String password) {
    return Base64.getEncoder().encodeToString(
      password.getBytes(StandardCharsets.UTF_8)
    );
  }

  public void doFilter(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain next
  ) throws ServletException, IOException {
    System.out.println("Executation du filtre");
    System.out.println(req.getParameter("password"));
    if (req.getParameter("password") != null) {
      String password = req.getParameter("password");
      String encryptedPassword = encrypt(password);
      req.setAttribute("encrypt", encryptedPassword);
    }
    next.doFilter(req, res);
  }
}
