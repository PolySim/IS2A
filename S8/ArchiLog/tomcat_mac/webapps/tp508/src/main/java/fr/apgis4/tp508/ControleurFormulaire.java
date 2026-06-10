package fr.apgis4.tp508;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.apgis4.tp508.Model.Etudiant;
import fr.apgis4.tp508.Repository.EtudiantRepository;
import jakarta.validation.Valid;
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

  @PostMapping("/formulaire")
  public String submitFormulaire(@Valid @ModelAttribute("etudiant") Etudiant etudiant,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
      return "formulaire";
    }

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
