package com.bookmaster.utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    private static final String DATA_DIR = "src/main/resources/data/";

    public static void initializeDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Répertoire de données créé: " + DATA_DIR);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du répertoire: " + e.getMessage());
        }
    }

    public static List<String> readCSV(String filename) {
        List<String> lines = new ArrayList<>();
        String filepath = DATA_DIR + filename;

        try {
            Path path = Paths.get(filepath);
            if (Files.exists(path)) {
                lines = Files.readAllLines(path);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de " + filename + ": " + e.getMessage());
        }

        return lines;
    }

    public static void writeCSV(String filename, List<String> lines) {
        String filepath = DATA_DIR + filename;

        try {
            Files.write(Paths.get(filepath), lines);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans " + filename + ": " + e.getMessage());
        }
    }

    public static boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR + filename));
    }

    public static int getNextId(String filename) {
        List<String> lines = readCSV(filename);
        if (lines.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        return maxId + 1;
    }
}
