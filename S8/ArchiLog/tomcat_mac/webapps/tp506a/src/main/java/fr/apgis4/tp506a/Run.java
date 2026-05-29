package fr.apgis4.tp506a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Run implements ApplicationRunner {

  @Autowired
  private MyOwnScheduling myOwnScheduling;

  @Autowired
  private MyOwnScheduling2 myOwnScheduling2;

  public void run(ApplicationArguments args) {
    myOwnScheduling.print();
    myOwnScheduling2.print();
  }
}
