package fr.apgis4;

import fr.apgis4.model.Auteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
      "archilog"
    );

    try {
      EntityManager em = emf.createEntityManager();

      try {
        Auteur auteur = em.find(Auteur.class, "alan.turing@example.com");
        System.out.println(auteur);
      } finally {
        em.close();
      }
    } finally {
      emf.close();
    }
  }
}
