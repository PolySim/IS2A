package servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/verif")
public class Verif extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    String username = (String) req.getParameter("username");
    String password = (String) req.getParameter("password");

    Connection connect = (Connection) getServletContext().getAttribute(
      "connect"
    );
    if (connect == null) {
      throw new ServletException(
        "Connexion BDD introuvable dans le ServletContext"
      );
    }

    try {
      PreparedStatement stmt = connect.prepareStatement(
        "SELECT * FROM utilisateurs WHERE login = ? AND mdp = md5(?)"
      );
      stmt.setString(1, username);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        HttpSession session = req.getSession();
        session.setAttribute("token", "isValid");
        session.setAttribute("username", username);
        String origin = (String) session.getAttribute("origin");
        session.removeAttribute("origin");

        if (origin == null) {
          origin = "/private/page1.jsp";
        }

        stmt = connect.prepareStatement(
          "UPDATE utilisateurs SET date = NOW(), ip = ? WHERE login = ?"
        );
        stmt.setString(1, req.getRemoteAddr());
        stmt.setString(2, username);
        stmt.executeUpdate();

        res.sendRedirect(req.getContextPath() + origin);
      } else {
        req.setAttribute("msg", "Invalid username or password");
        req.getRequestDispatcher("/login.jsp").forward(req, res);
      }
    } catch (SQLException e) {
      throw new ServletException(e);
    }
  }
}
