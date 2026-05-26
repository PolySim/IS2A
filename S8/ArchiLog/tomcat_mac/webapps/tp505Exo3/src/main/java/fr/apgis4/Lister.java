package fr.apgis4;

import fr.apgis4.model.Auteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class Lister {

  private List<Auteur> auteurs;

  public List<Auteur> getAuteurs() {
    return auteurs;
  }

  public void setAuteurs(List<Auteur> auteurs) {
    this.auteurs = auteurs;
  }

  public Lister() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
      "archilog"
    );

    try {
      EntityManager em = emf.createEntityManager();

      try {
        auteurs = em
          .createQuery("SELECT a FROM Auteur a", Auteur.class)
          .getResultList();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      } finally {
        em.close();
      }
    } finally {
      emf.close();
    }
  }
}
