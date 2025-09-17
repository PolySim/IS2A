import exceptions.NomVideException;
import lib.Article;
import lib.Rayon;

public class TesteRayon {
    public static void main(String[] args) {
        Rayon r1 = new Rayon(4);

        try {
        Article a1 = new Article("Velo","alimentaire", 299.99);
        r1.add(a1);
        } catch (NomVideException e) {
            System.out.println(e.getMessage());
        }
        try {
        Article a2 = new Article("Short","textile", 99.99);
        r1.add(a2);
        } catch (NomVideException e) {
            System.out.println(e.getMessage());
        }
        try {
        Article a3 = new Article("Casquettszzsze","textile", 199.99);
        r1.add(a3);
        r1.remove(2);
        } catch (NomVideException e) {
            System.out.println(e.getMessage());
        }
        try {
        Article a4 = new Article("Lavabo","maison", 30.0);
        r1.add(a4);
        } catch (NomVideException e) {
            System.out.println(e.getMessage());
        }


        System.out.println(r1);
    }
}
