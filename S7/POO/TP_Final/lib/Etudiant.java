package lib;

import exceptions.PasMajeurException;

public class Etudiant {
  private String nom;
  private int age;
  private int id;

  private static int next_id = 0;

  private final static String DEFAULT_NAME = "Jean";
  private final static int DEFAULT_AGE = 18;
  final static int AGE_MAJORITE = 18;

  public Etudiant(String new_nom, int new_age) throws PasMajeurException {
    this.setAge(new_age);
    this.getAge();
    this.setNom(new_nom);

    // Utile seulement ici car réutilisation de ce constructeur partout
    this.id = next_id;
    Etudiant.increment_id();
  }

  public Etudiant() throws PasMajeurException {
    this(Etudiant.DEFAULT_NAME, Etudiant.DEFAULT_AGE);
  }

  public Etudiant(Etudiant last_etudiant) throws PasMajeurException {
    this(last_etudiant.nom, last_etudiant.age);
  }

  private static void increment_id() {
    Etudiant.next_id += 1;
  }

  public int getId() {
    return this.id;
  }

  public void setNom(String new_nom) {
    this.nom = new_nom;
  }

  public String getNom() {
    return this.nom;
  }

  public void setAge(int new_age) {
    this.age = new_age;
  }

  // Fonction pour récupérer l'âge seulement pour s'est enfant. On ne met pas de
  // controller car la réel valeur de l'âge sera redéfinie
  int getAgeByPass() {
    return this.age;
  }

  public int getAge() throws PasMajeurException {
    this.controle_majorite(this.age);
    return this.age;
  }

  public void controle_majorite(int age) throws PasMajeurException {
    if (age < Etudiant.AGE_MAJORITE) {
      throw new PasMajeurException();
    }
  }

  public String toString() {
    try {
      return "{" + this.getNom() + ", " + this.getAge() + "}";
    } catch (PasMajeurException e) {
      return "";
    }
  }
}
