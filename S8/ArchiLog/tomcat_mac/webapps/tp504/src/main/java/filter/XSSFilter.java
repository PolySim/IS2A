package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;

@WebFilter(urlPatterns = { "/*" })
public class XSSFilter extends HttpFilter {

  public void doFilter(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain next
  ) throws ServletException, IOException {
    if (req.getParameter("msg") != null) {
      String msg = req.getParameter("msg");
      msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
      req.setAttribute("msg", msg);
    } else {
      req.setAttribute("msg", "");
    }
    next.doFilter(req, res);
  }
}
