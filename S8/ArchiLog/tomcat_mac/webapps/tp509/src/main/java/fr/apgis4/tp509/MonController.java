package fr.apgis4.tp509;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MonController {

private final JdbcTemplate jdbcTemplate;

  public MonController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @RequestMapping("/public")
  public String publicV1() {
    return "public/v1";
  }

  @RequestMapping("/private")
  public String privateV2(HttpServletRequest request, Model model) {
    String name = request.getRemoteUser();
    model.addAttribute("name", name);
    return "private/v2";
  }

  @RequestMapping("/private-auth")
  public String privateV2Auth(Authentication authentication, Model model) {
    String name = authentication != null ? authentication.getName() : null;
    model.addAttribute("name", name);
    return "private/v2";
  }

  @RequestMapping("/users")
  public String users(Model model) {
    List<UserRow> users = jdbcTemplate.query(
        "select username, password, enabled from users order by username",
        (rs, rowNum) -> new UserRow(
            rs.getString("username"),
            rs.getString("password"),
            rs.getBoolean("enabled")));
    model.addAttribute("users", users);
    return "private/users";
  }

  public record UserRow(String username, String password, boolean enabled) {
  }
}
