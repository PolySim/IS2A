package fr.apgis4.tp508;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.apgis4.tp508.Model.Etudiant;
import fr.apgis4.tp508.Repository.EtudiantRepository;

@RestController
public class ControleurEtudiantRes {

  @Autowired
  private EtudiantRepository etudiantRepository;

  @GetMapping("/etudiants")
  public Iterable<Etudiant> listeEtudiants() {
    return etudiantRepository.findAll();
  }

  @GetMapping("/etudiants/{id}")
  public ResponseEntity<Object> getEtudiantById(@PathVariable int id) {
    Optional<Etudiant> etudiant = etudiantRepository.findById(id);
    if (etudiant.isPresent()) {
      return ResponseEntity.ok(etudiant.get());
    } else {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "L'étudiant avec l'id " + id + " n'existe pas");
      error.put("status", 404);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  }

  @GetMapping("/etudiants/select")
  public ResponseEntity<Object> selectEtudiant(@RequestParam String groupe) {
    Iterable<Etudiant> etudiants = etudiantRepository.findByGroupeOrderByNom(groupe);
    if (etudiants.iterator().hasNext()) {
      return ResponseEntity.ok(etudiants);
    } else {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "Aucun étudiant trouvé pour le groupe " + groupe);
      error.put("status", 404);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  }
}
