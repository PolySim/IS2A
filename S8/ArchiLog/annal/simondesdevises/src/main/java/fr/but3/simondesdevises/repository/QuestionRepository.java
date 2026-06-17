package fr.but3.simondesdevises.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.but3.simondesdevises.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

	@EntityGraph(attributePaths = "choix")
	List<Question> findAllByOrderByQnoAsc();

	@EntityGraph(attributePaths = "choix")
	Optional<Question> findByActiveTrue();
}
