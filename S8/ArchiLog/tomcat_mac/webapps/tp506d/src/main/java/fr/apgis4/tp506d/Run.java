package fr.apgis4.tp506d;

import fr.apgis4.tp506d.Repository.AuterRepository;
import fr.apgis4.tp506d.Repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Run implements ApplicationRunner {

  @Autowired
  private AuterRepository auterRepository;

  @Autowired
  private LivreRepository livreRepository;

  @Transactional
  public void run(ApplicationArguments args) {
    auterRepository.findAll().forEach(auteur -> {
      System.out.println(auteur.printAuteurWithLivre());
    });
    livreRepository
      .findByTitreLikeOrderByLnoDesc("%the%")
      .forEach(livre -> System.out.println(livre));
  }
}
