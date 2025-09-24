import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import elements.Bucher;
import elements.Buchers;

class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        BufferedReader reader;
        Buchers buchers = new Buchers();

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            String[] tline;
            String x;
            String y;

            int id = 1;
            while (line != null && !line.equals("")) {
                tline = line.split("\\s+");
                x = tline[1];
                y = tline[2];
                buchers.add(new Bucher(Float.parseFloat(x), Float.parseFloat(y), buchers, id));
                id++;
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(buchers.isConnexe());
    }
}
