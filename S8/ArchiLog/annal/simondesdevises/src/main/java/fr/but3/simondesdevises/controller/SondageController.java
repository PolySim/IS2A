package fr.but3.simondesdevises.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.but3.simondesdevises.model.Question;
import fr.but3.simondesdevises.service.SondageService;

@Controller
public class SondageController {

	private final SondageService sondageService;

	public SondageController(SondageService sondageService) {
		this.sondageService = sondageService;
	}

	@GetMapping("/")
	public String accueil(Authentication authentication) {
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"))) {
			return "redirect:/activer";
		}
		return "redirect:/voter";
	}

	@GetMapping("/activer")
	public String formulaireActivation(Model model, Authentication authentication) {
		model.addAttribute("questions", sondageService.listerQuestions());
		model.addAttribute("username", authentication.getName());
		return "activer";
	}

	@PostMapping("/activer")
	public String activerQuestion(@RequestParam Integer qno) {
		sondageService.activerQuestion(qno);
		return "redirect:/voter";
	}

	@GetMapping("/voter")
	public String formulaireVote(Model model, Authentication authentication) {
		Question question = sondageService.questionActive();
		model.addAttribute("question", question);
		model.addAttribute("username", authentication.getName());
		return "voter";
	}

	@PostMapping("/voter")
	public String voter(@RequestParam Integer cno) {
		sondageService.voter(cno);
		return "redirect:/voir";
	}

	@GetMapping("/confirmation-activation")
	@ResponseBody
	public String confirmationActivation(@RequestParam Integer qno) {
		Question question = sondageService.listerQuestions().stream()
				.filter(q -> q.getQno().equals(qno))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Question introuvable"));
		return "La question " + question.getLibelle() + " vient d'etre activee";
	}

	@GetMapping("/confirmation-vote")
	@ResponseBody
	public String confirmationVote() {
		return "Votre choix a bien ete pris en compte";
	}

	@GetMapping("/voir")
	public String voirResultats(Model model, Authentication authentication) {
		model.addAttribute("question", sondageService.questionActive());
		model.addAttribute("username", authentication.getName());
		return "voir";
	}
}
