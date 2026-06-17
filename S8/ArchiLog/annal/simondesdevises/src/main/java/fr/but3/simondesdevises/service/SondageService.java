package fr.but3.simondesdevises.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.but3.simondesdevises.model.Choix;
import fr.but3.simondesdevises.model.Question;
import fr.but3.simondesdevises.repository.ChoixRepository;
import fr.but3.simondesdevises.repository.QuestionRepository;

@Service
public class SondageService {

	private final QuestionRepository questionRepository;
	private final ChoixRepository choixRepository;

	public SondageService(QuestionRepository questionRepository, ChoixRepository choixRepository) {
		this.questionRepository = questionRepository;
		this.choixRepository = choixRepository;
	}

	public List<Question> listerQuestions() {
		return questionRepository.findAllByOrderByQnoAsc();
	}

	public Question questionActive() {
		return questionRepository.findByActiveTrue().orElse(null);
	}

	@Transactional
	public Question activerQuestion(Integer qno) {
		List<Question> questions = questionRepository.findAllByOrderByQnoAsc();
		Question questionActivee = null;

		for (Question question : questions) {
			boolean selectionnee = question.getQno().equals(qno);
			question.setActive(selectionnee);
			for (Choix choix : question.getChoix()) {
				choix.setNbChoix(0);
			}
			if (selectionnee) {
				questionActivee = question;
			}
		}

		if (questionActivee == null) {
			throw new IllegalArgumentException("Question introuvable");
		}

		return questionActivee;
	}

	@Transactional
	public void voter(Integer cno) {
		Choix choix = choixRepository.findByCnoAndQuestionActiveTrue(cno)
				.orElseThrow(() -> new IllegalArgumentException("Choix introuvable"));
		choix.setNbChoix(choix.getNbChoix() + 1);
	}
}
