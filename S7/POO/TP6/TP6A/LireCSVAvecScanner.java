import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import mediatheque.Bibliotheque;
import utils.WriteCsv;

public class LireCSVAvecScanner {
    public static void main(String[] args) {
        String cheminFichier = args[0];
        Bibliotheque bibliotheque = new Bibliotheque();
        WriteCsv writeCsv = new WriteCsv(bibliotheque, ",");

        int i = 0;
        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                if (i > 0) {
                    writeCsv.write(ligne);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(bibliotheque.show());
    }
}
