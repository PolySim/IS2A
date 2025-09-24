import lib.Pokemon;

class TestePokemon {
    public static void main(String[] args) {
        Pokemon p1 = new Pokemon("Bulbizarre", 450, 24);

        System.out.println(p1);

        System.out.println("Attaque 200");
        p1.enlevePV(200);

        System.out.println(p1);
        System.out.println(p1.estKO());

        System.out.println("Attaque 300");
        p1.enlevePV(300);

        System.out.println("Devrait être KO");
        System.out.println(p1.estKO());

        System.out.println("Gueri");
        p1.gueri();

        System.out.println(p1);

        Pokemon p2 = new Pokemon("Ramoloss", 900, 65);

        p1.attaque(p2);
        System.out.println(p1);
        System.out.println(p2);
    }
}
