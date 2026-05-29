package fr.apgis4.tp506a;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyOwnScheduling {

  @Scheduled(initialDelay = 2000, fixedRate = 500)
  public void print() {
    System.out.println("Hello from MyOwnScheduling");
  }
}
