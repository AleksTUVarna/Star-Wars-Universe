package main;

import java.util.Scanner;
import commands.StarWarsUniverse;
import models.Jedi;
import models.Rank;
import utils.FileUtils;

public class Main {
    public static void printGuide() {
        System.out.println("Welcome to the Star Wars Universe!");
        System.out.println("Available commands:");
        System.out.println("add_planet <name>");
        System.out.println("create_jedi <planet> <name> <rank> <age> <color> <strength>");
        System.out.println("remove_jedi <name> <planet>");
        System.out.println("promote_jedi <name> <multiplier>");
        System.out.println("demote_jedi <name> <multiplier>");
        System.out.println("get_strongest_jedi <planet>");
        System.out.println("get_youngest_jedi <planet> <rank>");
        System.out.println("get_most_used_saber_color <planet> <rank>");
        System.out.println("print <planet|jedi|planet1+planet2>");
        System.out.println("save <filename>");
        System.out.println("load <filename>");
        System.out.println("help");
        System.out.println("exit");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StarWarsUniverse uni = new StarWarsUniverse();
        printGuide();

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            String cmd = parts[0].toLowerCase();

            try {
                switch (cmd) {
                    case "add_planet":
                        if (uni.addPlanet(parts[1])) {
                            System.out.println("Planet added.");
                        } else {
                            System.out.println("Planet exists.");
                        }
                        break;
                    case "create_jedi":
                        Jedi j = new Jedi(parts[2], Rank.valueOf(parts[3].toUpperCase()),
                                Integer.parseInt(parts[4]), parts[5], Double.parseDouble(parts[6]));
                        if (uni.createJedi(parts[1], j)) {
                            System.out.println("Jedi created.");
                        } else {
                            System.out.println("Failed to create Jedi.");
                        }
                        break;
                    case "remove_jedi":
                        if (uni.removeJedi(parts[1], parts[2])) {
                            System.out.println("Jedi removed.");
                        } else {
                            System.out.println("Removal failed.");
                        }
                        break;
                    case "promote_jedi":
                        if (uni.promoteJedi(parts[1], Double.parseDouble(parts[2]))) {
                            System.out.println("Jedi promoted.");
                        } else {
                            System.out.println("Promotion failed.");
                        }
                        break;
                    case "demote_jedi":
                        if (uni.demoteJedi(parts[1], Double.parseDouble(parts[2]))) {
                            System.out.println("Jedi demoted.");
                        } else {
                            System.out.println("Demotion failed.");
                        }
                        break;
                    case "get_strongest_jedi":
                        Jedi s = uni.getStrongestJedi(parts[1]);
                        if (s != null) {
                            System.out.println(s);
                        } else {
                            System.out.println("No Jedi found.");
                        }
                        break;
                    case "get_youngest_jedi":
                        Jedi y = uni.getYoungestJedi(parts[1], Rank.valueOf(parts[2].toUpperCase()));
                        if (y != null) {
                            System.out.println(y);
                        } else {
                            System.out.println("No Jedi found.");
                        }
                        break;
                    case "get_most_used_saber_color":
                        if (parts.length == 3) {
                            String color = uni.getMostUsedSaberColor(parts[1], Rank.valueOf(parts[2].toUpperCase()));
                            System.out.println(color != null ? color : "None.");
                        } else {
                            String color = uni.getMostUsedSaberColor(parts[1]);
                            System.out.println(color != null ? color : "None.");
                        }
                        break;
                    case "print":
                        if (parts[1].contains("+")) {
                            String[] ps = parts[1].split("\\+");
                            uni.printUnion(ps[0], ps[1]);
                        } else if (uni.getPlanet(parts[1]) != null) {
                            uni.printPlanet(parts[1]);
                        } else {
                            uni.printJedi(parts[1]);
                        }
                        break;
                    case "save":
                        String outName = parts[1].toLowerCase().endsWith(".txt")
                                ? parts[1]
                                : parts[1] + ".txt";
                        FileUtils.save(outName, uni);
                        System.out.println("Saved to " + outName);
                        break;
                    case "load":
                        String inName = parts[1].toLowerCase().endsWith(".txt")
                                ? parts[1]
                                : parts[1] + ".txt";
                        uni = FileUtils.load(inName);
                        System.out.println("Loaded from " + inName);
                        break;
                    case "help":
                        printGuide();
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Unknown command. Type 'help' for guide.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
