package mediatheque;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import media.Livre;

public class Bibliotheque implements Serializable {
    private ArrayList<Livre> livres;

    public Bibliotheque(String filename) {
        this.livres = new ArrayList<Livre>();
        try {
            this.load(filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bibliotheque(ArrayList<Livre> livres) {
        this.livres = livres;
    }

    public Bibliotheque() {
        this.livres = new ArrayList<Livre>();
    }

    public void add(Livre newLivre) {
        livres.add(newLivre);
    }

    public int getSomme() {
        int somme = 0;
        for (Livre l : this.livres) {
            somme += l.getPrix();
        }
        return somme;
    }

    public String show() {
        String m = "Bibliotheque : ";
        for (Livre l : this.livres) {
            m += "[" + l.show() + "], ";
        }
        return m;
    }

    public void write(ArrayList<Livre> livres) {
        this.livres = livres;
        try {
            this.save("save.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            this.load("save.bin");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void save(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(this);
        out.close();
    }

    private void load(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        Bibliotheque b = (Bibliotheque) in.readObject();
        this.livres = b.livres;
        in.close();
    }

}
