package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lib.Gare;

public class VoirGare {
  private String filename;
  private final static String SEPARATOR = "\t";

  public VoirGare(String filename) {
    this.filename = filename;
  }

  public List<Gare> getGares() {
    List<Gare> gares = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(this.filename));
      gares = reader.lines()
          .skip(1)
          .map(line -> {
            String[] data = line.split(SEPARATOR);
            Gare gare = new Gare(data[0], data[1], data[2], data[3], data[4]);
            return gare;
          })
          .collect(Collectors.toList());
      reader.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return gares;
  }

}
