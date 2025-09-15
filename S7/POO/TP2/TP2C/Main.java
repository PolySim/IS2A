import lib.File;
import lib.ListeTab;
import lib.Pile;

class Main {
    public static void main(String[] args) {
        ListeTab t = new File();

        t.add(2);
        t.add(3);
        t.add(4);
        t.pop(); // pop 2

        t.add(5);
        t.add(6);
        t.add(7);

        int a = t.pop(); // pop 3

        System.out.println(t); // 4 5 6 7
        System.out.println(a); // 3
        t = new Pile();

        t.add(2);
        t.add(3);
        t.add(4);
        t.pop(); // pop 4

        t.add(5);
        t.add(6);
        t.add(7);

        a = t.pop(); // pop 7

        System.out.println(t); // 2 3 5 6

        System.out.println(a); // 7
    }
}
