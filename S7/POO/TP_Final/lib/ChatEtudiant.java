package lib;

import exceptions.PasMajeurException;

public class ChatEtudiant extends Etudiant {
  private int nombreDeVie;

  public ChatEtudiant(String nom, int age, int nbVie) throws PasMajeurException {
    super(nom, age);
    this.nombreDeVie = nbVie;
  }

  @Override
  public final int getAge() throws PasMajeurException {
    int new_age = super.getAgeByPass() * 5 + 10 * this.nombreDeVie;
    super.controle_majorite(new_age);
    return new_age;
  }
}
