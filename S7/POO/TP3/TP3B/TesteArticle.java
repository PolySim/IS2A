import lib.Article;
import exceptions.ProblemeCategorieException;

class TesteArticle {
    public static void main(String[] args) {
        System.out.println("Test de votre classe Article");

        try {
            Article a1 = new Article("Velo","alimentaire", 299.99);
            System.out.println(a1);
        } catch (ProblemeCategorieException e) {
            System.out.println("Problème de catégorie");
        }

        try {
            Article a1 = new Article("Short","textile", 99.99);
            System.out.println(a1);
        } catch (ProblemeCategorieException e) {
            System.out.println("Problème de catégorie");
        }

        try {
            Article a1 = new Article("Casquette","textile", 199.99);
            System.out.println(a1);
        } catch (ProblemeCategorieException e) {
            System.out.println("Problème de catégorie");
        }

        try {
            Article a1 = new Article("Lavabo","maison", 30.0);
            System.out.println(a1);
        } catch (ProblemeCategorieException e) {
            System.out.println("Problème de catégorie");
        }


        System.out.println("Fin du Test");
    }
}
