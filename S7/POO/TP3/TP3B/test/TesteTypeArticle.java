package test;

import exceptions.ProblemeCategorieException;
import lib.TypeArticle;

public class TesteTypeArticle extends TypeArticle {
  public TesteTypeArticle(String newCategorie) throws ProblemeCategorieException {
    super(newCategorie);
  }

  public static void main(String[] args) {
    try {
      TesteTypeArticle t1 = new TesteTypeArticle("alimentaire");
      System.out.println(t1);
    } catch (ProblemeCategorieException e) {
      System.out.println(e.getMessage());
    }
    try {
      TesteTypeArticle t2 = new TesteTypeArticle("maison");
      System.out.println(t2);
    } catch (ProblemeCategorieException e) {
      System.out.println(e.getMessage());
    }
  }
}
