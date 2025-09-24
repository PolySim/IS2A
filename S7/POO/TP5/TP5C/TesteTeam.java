import lib.Pokemon;
import lib.Team;

class TesteTeam {
    public static void main(String[] args) {
        Team t1 = new Team("Sacha");

        System.out.println(t1);

        Pokemon p1 = new Pokemon("Bulbizarre", 450, 24);
        Pokemon p2 = new Pokemon("Ramoloss", 900, 65);

        t1.addPokemon(p1);
        t1.addPokemon(p2);

        System.out.println("Taille : " + t1.tailleEquipe());

        Pokemon next = t1.nextCombatant();

        System.out.println(next);

        System.out.println(t1);
    }
}
