package fr.apgis4.tp507.Repository;

import org.springframework.data.repository.CrudRepository;

import fr.apgis4.tp507.Model.Etudiant;

public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
}
