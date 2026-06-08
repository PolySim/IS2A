package fr.apgis4.tp507;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControleurQ1 {

  @RequestMapping({ "/hello", "/" })
  @ResponseBody
  public String hello() {
    return "From Q1";
  }
}
