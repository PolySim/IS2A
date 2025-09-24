import lib.Pokemon;
import lib.Team;

class TesteTeamOrdre {
    public static void main(String[] args) {
        Pokemon p1 = new Pokemon("Bulbizarre", 450, 24);
        Pokemon p2 = new Pokemon("Ramoloss", 900, 65);
        Pokemon p3 = new Pokemon("Lippoutou", 650, 50);
        Pokemon p4 = new Pokemon("Nosferapti", 400, 45);

        Team t1 = new Team("Sacha");
        t1.addPokemon(p1);
        t1.addPokemon(p2);
        t1.addPokemon(p3);
        t1.addPokemon(p4);

        System.out.println(t1);

        t1.triParNom();
        System.out.println(t1);

        t1.triParPVMax();
        System.out.println(t1);

        t1.triParAttaque();
        System.out.println(t1);

    }
}
