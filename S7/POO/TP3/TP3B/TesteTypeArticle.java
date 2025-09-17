import lib.TypeArticle;

class TesteTypeArticle {
    public static void main(String[] args) {
        System.out.println("Test de votre classe TypeArticle");

        String m1 = "alimentaire";
        System.out.println("Test 1 avec \""+m1+"\" (OK ?)");
        TypeArticle t1 = new TypeArticle(m1);
        System.out.println(t1);

        String m2 = "ali";
        System.out.println("Test 1 avec \""+m2+"\" (KO ?)");
        TypeArticle t2 = new TypeArticle(m2);
        System.out.println(t2);

        String m3 = "textile";
        System.out.println("Test 1 avec \""+m3+"\" (OK ?)");
        TypeArticle t3 = new TypeArticle(m3);
        System.out.println(t3);

        String m4 = "electromenager";
        System.out.println("Test 1 avec \""+m4+"\" (OK ?)");
        TypeArticle t4 = new TypeArticle(m4);
        System.out.println(t4);

        String m5 = "cosmetique";
        System.out.println("Test 1 avec \""+m5+"\" (OK ?)");
        TypeArticle t5 = new TypeArticle(m5);
        System.out.println(t5);

        String m6 = "maison";
        System.out.println("Test 1 avec \""+m6+"\" (KO ?)");
        TypeArticle t6 = new TypeArticle(m6);
        System.out.println(t6);

        System.out.println("Fin du Test");
    }
}
