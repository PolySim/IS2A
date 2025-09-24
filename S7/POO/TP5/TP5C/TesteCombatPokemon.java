import lib.*;

class TesteCombatPokemon {
    public static void main(String[] args) {
        Pokemon p1 = new Pokemon("Bulbizarre", 450, 24);
        Pokemon p2 = new Pokemon("Ramoloss", 900, 65);

        Pokemon gagnant = p1.combat(p2);
        if (gagnant == null) {
            System.out.println("Pas de gagnant");
        } else {
            System.out.println("Gagnant : " + gagnant);
        }
    }
}
