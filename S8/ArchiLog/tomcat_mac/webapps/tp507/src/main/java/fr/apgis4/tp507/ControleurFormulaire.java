package fr.apgis4.tp507;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.apgis4.tp507.Model.Etudiant;
import lombok.Getter;
import lombok.Setter;

@Controller
@Getter
@Setter
public class ControleurFormulaire {
  private ArrayList<Etudiant> etudiants = new ArrayList<>();

  @RequestMapping(value = "/formulaire", method = RequestMethod.GET)
  public String viewFormulaire() {
    return "formulaire";
  }

  @RequestMapping(value = "/formulaire", method = RequestMethod.POST)
  public String submitFormulaire(Etudiant etudiant, Model model) {
    etudiants.add(etudiant);
    return "redirect:/liste";
  }

  @RequestMapping(value = "/liste")
  public String liste(Model model) {
    model.addAttribute("etudiants", etudiants);
    return "liste";
  }
}
