package fr.apgis4.tp507;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControleurQ2 {

  @RequestMapping("/q2")
  public String hello() {
    return "mavue";
  }
}
