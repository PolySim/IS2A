package fr.apgis4.tp506a;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyOwnScheduling2 {

  @Scheduled(initialDelay = 2000, fixedRate = 1000)
  public void print() {
    System.out.println("Hello from MyOwnScheduling 2");
  }
}
