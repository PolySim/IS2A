package lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparator.ComparateurAttaque;
import comparator.ComparateurPVMax;

public class Team {
  String nom;
  List<Pokemon> pokemons;
  int current;

  public Team(String nom) {
    this.nom = nom;
    this.pokemons = new ArrayList<>();
    this.current = 0;
  }

  public String getNom() {
    return this.nom;
  }

  public void addPokemon(Pokemon p) {
    this.pokemons.add(p);
  }

  public int tailleEquipe() {
    return this.pokemons.size();
  }

  public Pokemon currentPokemon() {
    return this.pokemons.get(this.current);
  }

  public Pokemon nextCombatant() {
    this.current++;
    if (this.current == this.pokemons.size()) {
      this.current = 0;
    }
    return this.pokemons.get(this.current);
  }

  public void gueri() {
    for (Pokemon p : this.pokemons) {
      p.gueri();
    }
  }

  public void triParNom() {
    Collections.sort(this.pokemons);
    this.current = 0;
  }

  public void triParPVMax() {
    Collections.sort(this.pokemons, new ComparateurPVMax());
    this.current = 0;
  }

  public void triParAttaque() {
    Collections.sort(this.pokemons, new ComparateurAttaque());
    this.current = 0;
  }

  public boolean estKO() {
    for (Pokemon p : this.pokemons) {
      if (!p.estKO()) {
        return false;
      }
    }
    return true;
  }

  public String toString() {
    String result = "";
    result += "Team : " + this.nom + " Pokemons : ";
    for (Pokemon p : this.pokemons) {
      result += p.toString() + "\n";
    }
    return result;
  }

  public Team combat(Team other) {
    while (!this.estKO() && !other.estKO()) {
      Pokemon gagnant = this.currentPokemon().combat(other.currentPokemon());
      if (gagnant.equals(this.currentPokemon())) {
        other.nextCombatant();
      } else {
        this.nextCombatant();
      }
    }
    return this.estKO() ? other : this;
  }

}
