import lib.Article;
import lib.Rayon;


public class TesteRayonArrayPointer {
    public static void main(String[] args) {
        Rayon r1 = new Rayon(2);

        Article a1 = new Article("Velo","alimentaire", 299.99);
        Article a2 = new Article("Short","textile", 99.99);
        Article a3 = new Article("Casquette","textile", 199.99);
        Article a4 = new Article("Lavabo","maison", 30.0);

        r1.add(a1);
        r1.add(a2);
        r1.add(a3);
        r1.remove(2);
        r1.add(a4);

        System.out.println(r1);
    }
}
