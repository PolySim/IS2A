package lib;

public class Pokemon implements Comparable<Pokemon> {
  String nom;
  int pv;
  final int PV_MAX;
  int attaque;

  public Pokemon(String nom, int pvMax, int attaque) {
    this.nom = nom;
    this.PV_MAX = pvMax;
    this.pv = pvMax;
    this.attaque = attaque;
  }

  public String getNom() {
    return this.nom;
  }

  public int getPv() {
    return this.pv;
  }

  public int getPVMax() {
    return this.PV_MAX;
  }

  public int getAttaque() {
    return this.attaque;
  }

  public boolean estKO() {
    return this.pv <= 0;
  }

  public void enlevePV(int nb) {
    this.pv -= nb;
  }

  public void gueri() {
    this.pv = this.PV_MAX;
  }

  public String toString() {
    return "Pokemon : " + this.nom + " PV : " + this.pv + " PVMax : " + this.PV_MAX + " Attaque : " + this.attaque;
  }

  public void attaque(Pokemon autrePokemon) {
    autrePokemon.enlevePV(this.attaque);
  }

  public int compareTo(Pokemon other) {
    return this.nom.compareTo(other.nom);
  }

  public Pokemon combat(Pokemon other) {
    int round = 0;
    while (!this.estKO() && !other.estKO()) {
      if (round % 2 == 0) {
        this.attaque(other);
      } else {
        other.attaque(this);
      }
      round++;
    }
    return this.estKO() ? other : this;
  }

  public boolean equals(Pokemon other) {
    return this.nom.equals(other.nom);
  }

}
