import lib.Pokemon;
import lib.Team;

class TesteCombatTeam {
    public static void main(String[] args) {
        Pokemon p1 = new Pokemon("Bulbizarre", 450, 24);
        Pokemon p2 = new Pokemon("Ramoloss", 900, 65);
        Pokemon p3 = new Pokemon("Lippoutou", 650, 50);
        Pokemon p4 = new Pokemon("Nosferapti", 400, 45);

        Pokemon p5 = new Pokemon("Taupiqueur", 80, 85);
        Pokemon p6 = new Pokemon("Electrode", 600, 50);
        Pokemon p7 = new Pokemon("Tartard", 870, 30);
        Pokemon p8 = new Pokemon("Rondoudou", 1150, 46);

        Team t1 = new Team("Sacha");
        t1.addPokemon(p1);
        t1.addPokemon(p2);
        t1.addPokemon(p3);
        t1.addPokemon(p4);

        Team t2 = new Team("Chen");
        t2.addPokemon(p5);
        t2.addPokemon(p6);
        t2.addPokemon(p7);
        t2.addPokemon(p8);

        Team gagnant = t1.combat(t2);
        if (gagnant == null) {
            System.out.println("Pas de gagnant");
        } else {
            System.out.println("Gagnant : " + gagnant);
        }

        t1.gueri();
        t2.gueri();

        t1.triParAttaque();

        Team gagnant2 = t1.combat(t2);
        if (gagnant2 == null) {
            System.out.println("Pas de gagnant");
        } else {
            System.out.println("Gagnant : " + gagnant2);
        }
    }
}
