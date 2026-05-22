package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;

@WebFilter(urlPatterns = { "/private/*" })
public class AuthGuard extends HttpFilter {

  public void doFilter(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain next
  ) throws ServletException, IOException {
    HttpSession session = req.getSession();
    if (session.getAttribute("token") != null) {
      next.doFilter(req, res);
    } else {
      session.setAttribute(
        "origin",
        req.getRequestURI().substring(req.getContextPath().length())
      );
      req.getRequestDispatcher("/login.jsp").forward(req, res);
    }
  }
}
