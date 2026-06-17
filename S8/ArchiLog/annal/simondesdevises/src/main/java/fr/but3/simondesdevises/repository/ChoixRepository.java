package fr.but3.simondesdevises.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.but3.simondesdevises.model.Choix;

public interface ChoixRepository extends JpaRepository<Choix, Integer> {

	Optional<Choix> findByCnoAndQuestionActiveTrue(Integer cno);
}
