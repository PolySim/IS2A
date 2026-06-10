package fr.apgis4.tp508.Repository;

import org.springframework.data.repository.CrudRepository;

import fr.apgis4.tp508.Model.Etudiant;

public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
}
