package utils;

import media.Auteur;
import media.OuvrageMono;
import mediatheque.Bibliotheque;

public class WriteCsv {
  private Bibliotheque bibliotheque;
  private String separator;

  public WriteCsv(Bibliotheque bibliotheque, String separator) {
    this.bibliotheque = bibliotheque;
    this.separator = separator;
  }

  public void write(String line) {
    String[] donnees = line.split(this.separator);
    bibliotheque.add(new OuvrageMono(new Auteur(donnees[0]), donnees[1], Integer.parseInt(donnees[2])));
  }
}
