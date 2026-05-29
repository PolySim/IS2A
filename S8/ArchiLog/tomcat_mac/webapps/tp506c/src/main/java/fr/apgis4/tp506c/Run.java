package fr.apgis4.tp506c;

import fr.apgis4.tp506c.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Run implements ApplicationRunner {

  @Autowired
  private UserRepository userRepository;

  public void run(ApplicationArguments args) {
    userRepository.findAll().forEach(user -> System.out.println(user));
  }
}
