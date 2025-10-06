package lib;

import exceptions.PasMajeurException;

public class ChienEtudiant extends Etudiant {
  public ChienEtudiant(String nom, int age) throws PasMajeurException {
    super(nom, age);
  }

  @Override
  public final int getAge() throws PasMajeurException {
    int new_age = super.getAgeByPass() * 7;
    super.controle_majorite(new_age);
    return new_age;
  }
}
