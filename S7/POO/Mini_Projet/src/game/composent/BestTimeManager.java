package game.composent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BestTimeManager {
  private static final String FILE_NAME = "best_times.csv";
  private static final String HEADER = "size,bombe,time";

  /**
   * Classe interne pour représenter une configuration de jeu et son temps
   */
  public static class BestTime {
    private int size;
    private int bombe;
    private long time;

    public BestTime(int size, int bombe, long time) {
      this.size = size;
      this.bombe = bombe;
      this.time = time;
    }

    public int getSize() {
      return this.size;
    }

    public int getBombe() {
      return this.bombe;
    }

    public long getTime() {
      return this.time;
    }

    public void setTime(long time) {
      this.time = time;
    }

    public String toCsvLine() {
      return this.size + "," + this.bombe + "," + this.time;
    }

    public static BestTime fromCsvLine(String line) {
      String[] parts = line.split(",");
      if (parts.length != 3) {
        throw new IllegalArgumentException("Format CSV invalide");
      }
      return new BestTime(
          Integer.parseInt(parts[0].trim()),
          Integer.parseInt(parts[1].trim()),
          Long.parseLong(parts[2].trim()));
    }

    public boolean matches(int size, int bombe) {
      return this.size == size && this.bombe == bombe;
    }
  }

  private static List<BestTime> loadBestTimes() {
    List<BestTime> bestTimes = new ArrayList<>();
    File file = new File(FILE_NAME);

    if (!file.exists()) {
      return bestTimes;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = reader.readLine(); // Ignorer le header
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          try {
            bestTimes.add(BestTime.fromCsvLine(line));
          } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de la ligne: " + line);
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
    }

    return bestTimes;
  }

  private static void saveBestTimes(List<BestTime> bestTimes) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
      writer.write(HEADER);
      writer.newLine();
      for (BestTime bestTime : bestTimes) {
        writer.write(bestTime.toCsvLine());
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("Erreur lors de l'écriture du fichier: " + e.getMessage());
    }
  }

  public static Long getBestTime(int size, int bombe) {
    List<BestTime> bestTimes = loadBestTimes();
    for (BestTime bestTime : bestTimes) {
      if (bestTime.matches(size, bombe)) {
        return bestTime.getTime();
      }
    }
    return null;
  }

  public static boolean saveBestTime(int size, int bombe, long time) {
    List<BestTime> bestTimes = loadBestTimes();
    boolean isNewRecord = false;

    BestTime existingBestTime = null;
    for (BestTime bestTime : bestTimes) {
      if (bestTime.matches(size, bombe)) {
        existingBestTime = bestTime;
        break;
      }
    }

    if (existingBestTime != null) {
      if (time < existingBestTime.getTime()) {
        existingBestTime.setTime(time);
        isNewRecord = true;
      }
    } else {
      bestTimes.add(new BestTime(size, bombe, time));
      isNewRecord = true;
    }

    if (isNewRecord) {
      saveBestTimes(bestTimes);
    }

    return isNewRecord;
  }

  /**
   * Formate un temps en secondes en format mm:ss
   */
  public static String formatTime(long seconds) {
    long minutes = seconds / 60;
    long remainingSeconds = seconds % 60;
    return String.format("%02d:%02d", minutes, remainingSeconds);
  }
}
