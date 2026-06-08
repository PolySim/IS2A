package fr.apgis4.tp507;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.apgis4.tp507.Model.Etudiant;
import fr.apgis4.tp507.Repository.EtudiantRepository;
import lombok.Getter;
import lombok.Setter;

@Controller
@Getter
@Setter
public class ControleurFormulaire {
  // private ArrayList<Etudiant> etudiants = new ArrayList<>();
  @Autowired
  private EtudiantRepository etudiantRepository;

  @RequestMapping(value = "/formulaire", method = RequestMethod.GET)
  public String viewFormulaire() {
    return "formulaire";
  }

  @RequestMapping(value = "/formulaire", method = RequestMethod.POST)
  public String submitFormulaire(Etudiant etudiant, Model model) {
    // etudiants.add(etudiant);
    etudiantRepository.save(etudiant);
    return "redirect:/liste";
  }

  @RequestMapping(value = "/liste")
  public String liste(Model model) {
    // model.addAttribute("etudiants", etudiants);
    model.addAttribute("etudiants", etudiantRepository.findAll());
    return "liste";
  }
}
