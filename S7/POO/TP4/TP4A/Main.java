import media.Auteur;
import media.Livre;
import media.OuvrageColl;
import media.OuvrageMono;
import media.Poster;
import mediatheque.BibliothequeParam;

class Main {
    public static void main(String[] args) {

        // Main
        Auteur zola = new Auteur("Zola");
        OuvrageMono nana = new OuvrageMono(zola, "Nana",19);
        System.out.println(nana.show());
        // FIN Main


        BibliothequeParam<OuvrageMono> b = new BibliothequeParam<OuvrageMono>();
        b.add(new OuvrageMono(zola, "Nana",19));
        b.add(new OuvrageMono(zola, "Germinal",24));
        System.out.println(b.getSomme());
        System.out.println(b.show());

        BibliothequeParam<OuvrageColl> b2 = new BibliothequeParam<OuvrageColl>();
        b2.add(new OuvrageColl(new Auteur[] {zola}, "Nana",19));
        b2.add(new OuvrageColl(new Auteur[] {zola}, "Claude Gueux",5));
        System.out.println(b2.getSomme());
        System.out.println(b2.show());

        BibliothequeParam<Livre> b3 = new BibliothequeParam<Livre>();
        b3.add(new OuvrageMono(zola, "Nana",19));
        b3.add(new OuvrageColl(new Auteur[] {zola}, "Claude Gueux",5));
        System.out.println(b3.getSomme());
        System.out.println(b3.show());

        BibliothequeParam<Poster> b4 = new BibliothequeParam<Poster>();
        b4.add(new Poster(10));
        System.out.println(b4.getSomme());
        System.out.println(b4.show());
       
    }
}
