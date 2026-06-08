package fr.apgis4.tp507;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("nombre")
public class ControleurCompteur {

  @GetMapping("/compteur")
  public String compteur(Model model, @SessionAttribute(required = false) Integer nombre) {
    if (nombre == null) {
      nombre = 0;
    }
    nombre++;
    model.addAttribute("nombre", nombre);
    return "compteur";
  }

  @PostMapping("/compteur")
  public String reset(Model model, @SessionAttribute(required = false) Integer nombre) {
    nombre = 0;
    model.addAttribute("nombre", nombre);
    return "redirect:/compteur";
  }

}
