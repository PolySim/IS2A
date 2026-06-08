package fr.apgis4.tp507;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControleurQ4 {

  @RequestMapping("/name")
  public String hello(@RequestParam(defaultValue = "World", required = false) String name, ModelMap model) {
    model.put("name", name);
    return "hello";
  }
}
