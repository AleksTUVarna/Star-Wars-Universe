package utils;

import java.io.*;
import java.util.*;
import commands.StarWarsUniverse;
import models.Jedi;
import models.Planet;
import models.Rank;

public class FileUtils {
    //данните във файлове .txt
    public static void save(String filename, StarWarsUniverse uni) throws IOException {
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // първо планети
            for (Planet p : uni.getPlanets()) {
                writer.println("PLANET:" + p.getName());
                // след това всички джедаи на тази планета
                for (Jedi j : p.getJedis()) {
                    writer.printf("JEDI:%s|%s|%s|%d|%s|%.2f%n",
                            p.getName(),
                            j.getName(),
                            j.getRank().name(),
                            j.getAge(),
                            j.getSaberColor(),
                            j.getStrength()
                    );
                }
            }
        }
    }

    // зарежда данните от .txt файл във StarWarsUniverse
    public static StarWarsUniverse load(String filename) throws IOException {
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }
        StarWarsUniverse uni = new StarWarsUniverse();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PLANET:")) {
                    String planetName = line.substring(7);
                    uni.addPlanet(planetName);
                } else if (line.startsWith("JEDI:")) {
                    String[] parts = line.substring(5).split("\\|");
                    // трябва да са 6 части planet|name|rank|age|color|strength
                    if (parts.length == 6) {
                        String pl = parts[0];
                        String name = parts[1];
                        Rank rank = Rank.valueOf(parts[2]);
                        int age = Integer.parseInt(parts[3]);
                        String color = parts[4];
                        double str = Double.parseDouble(parts[5]);
                        Jedi j = new Jedi(name, rank, age, color, str);
                        uni.createJedi(pl, j);
                    }
                }
            }
        }
        return uni;
    }
}
