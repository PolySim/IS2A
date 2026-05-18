package fr.isia4;

import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        try {
            String data = loadResource("data.txt");
            System.out.println(data);

            DbContext dbContext = new DbContext();
            System.out.println("Db connected");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadResource(String resourceName) throws IOException {
        ClassLoader classLoader = App.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourceName);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
