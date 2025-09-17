import exceptions.TacheDejaExistanteException;
import lib.Arc.Arc;
import lib.Tache.TacheComposee;
import lib.Tache.TacheSimple;
import lib.Workflow.Workflow;

public class Main {
  public static void main(String[] args) {
    TacheSimple tache1 = new TacheSimple("1", 10, 100);
    TacheSimple tache2 = new TacheSimple("2", 20, 200);
    TacheSimple tache3 = new TacheSimple("2", 30, 300);
    TacheComposee tache4 = new TacheComposee("4", new TacheSimple[] {tache1, tache2, tache3});
    TacheSimple tache5 = new TacheSimple("5", 40, 400);

    Workflow workflow = new Workflow();
    try {
      workflow.getTaches().ajouterTache(tache1);
    } catch (TacheDejaExistanteException e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.getTaches().ajouterTache(tache2);
    } catch (TacheDejaExistanteException e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.getTaches().ajouterTache(tache3);
    } catch (TacheDejaExistanteException e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.getTaches().ajouterTache(tache4);
    } catch (TacheDejaExistanteException e) {
      System.out.println(e.getMessage());
    }

    Arc arc1 = new Arc(tache1, tache2);
    Arc arc2 = new Arc(tache2, tache3);
    Arc arc3 = new Arc(tache3, tache4);
    Arc arc4 = new Arc(tache4, tache5);
    Arc arc5 = new Arc(tache4, tache1);
    
    try { 
    workflow.ajouterArc(arc1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.ajouterArc(arc2);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.ajouterArc(arc3);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.ajouterArc(arc4);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    try {
      workflow.ajouterArc(arc5);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    System.out.println(workflow);
  }
}
