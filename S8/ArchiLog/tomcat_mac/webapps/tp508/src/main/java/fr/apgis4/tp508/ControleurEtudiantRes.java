package fr.apgis4.tp508;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.apgis4.tp508.Model.Etudiant;
import fr.apgis4.tp508.Repository.EtudiantRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ControleurEtudiantRes {

  @Autowired
  private EtudiantRepository etudiantRepository;

  @GetMapping("/etudiants")
  public ResponseEntity<Iterable<Etudiant>> listeEtudiants() {
    return ResponseEntity.ok(etudiantRepository.findAll());
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

  @PostMapping(value = "/etudiants", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Object> ajouterEtudiant(@Valid @RequestBody Etudiant etudiant) {
    // if (bindingResult.hasErrors()) {
    // return ResponseEntity.badRequest().body("Il y a des erreurs dans les données
    // envoyées");
    // }
    Etudiant savedEtudiant = etudiantRepository.save(etudiant);
    return ResponseEntity.ok(savedEtudiant);
  }

  @PutMapping(value = "/etudiants/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Object> modifierEtudiant(@PathVariable int id, @RequestBody Etudiant etudiant) {
    Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
    if (existingEtudiant.isPresent()) {
      Etudiant updatedEtudiant = existingEtudiant.get();
      updatedEtudiant.setNom(etudiant.getNom());
      updatedEtudiant.setPrenom(etudiant.getPrenom());
      updatedEtudiant.setAge(etudiant.getAge());
      updatedEtudiant.setGroupe(etudiant.getGroupe());
      Etudiant savedEtudiant = etudiantRepository.save(updatedEtudiant);
      return ResponseEntity.ok(savedEtudiant);
    } else {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "L'étudiant avec l'id " + id + " n'existe pas");
      error.put("status", 404);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  }

  @PatchMapping(value = "/etudiants/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Object> modifierEtudiantPartiellement(@PathVariable int id, @RequestBody Etudiant etudiant) {
    Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
    if (existingEtudiant.isPresent()) {
      Etudiant updatedEtudiant = existingEtudiant.get();
      if (etudiant.getNom() != null) {
        updatedEtudiant.setNom(etudiant.getNom());
      }
      if (etudiant.getPrenom() != null) {
        updatedEtudiant.setPrenom(etudiant.getPrenom());
      }
      if (etudiant.getAge() != null) {
        updatedEtudiant.setAge(etudiant.getAge());
      }
      if (etudiant.getGroupe() != null) {
        updatedEtudiant.setGroupe(etudiant.getGroupe());
      }
      Etudiant savedEtudiant = etudiantRepository.save(updatedEtudiant);
      return ResponseEntity.ok(savedEtudiant);
    } else {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "L'étudiant avec l'id " + id + " n'existe pas");
      error.put("status", 404);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  }

  @DeleteMapping("/etudiants/{id}")
  public ResponseEntity<Object> supprimerEtudiant(@PathVariable int id) {
    Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
    if (existingEtudiant.isPresent()) {
      etudiantRepository.delete(existingEtudiant.get());
      return ResponseEntity.ok().build();
    } else {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "L'étudiant avec l'id " + id + " n'existe pas");
      error.put("status", 404);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  }

}
