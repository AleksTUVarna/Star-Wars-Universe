package commands;

import java.io.Serializable;
import java.util.Collection;
import java.util.*;
import models.Jedi;
import models.Planet;
import models.Rank;

public class StarWarsUniverse implements Serializable {
    private Map<String, Planet> planets = new HashMap<>();
    private Map<String, String> jediLocation = new HashMap<>();

    // връща колекция от всички планети
    public Collection<Planet> getPlanets() {
        return planets.values();
    }

    // връща планета по име (без разлика в главни/малки букви!!!)
    public Planet getPlanet(String name) {
        if (name == null) {
            return null;
        }
        return planets.get(name.toLowerCase());
    }

    // добавя нова планета
    public boolean addPlanet(String name) {
        String key = name.toLowerCase();
        if (planets.containsKey(key)) {
            return false;
        }
        planets.put(key, new Planet(name));
        return true;
    }

    // създава нов джедай на планетата, ако е валиден
    public boolean createJedi(String planetName, Jedi jedi) {
        if (jedi == null) {
            return false;
        }
        // проверка на сила между 1 и 2
        double str = jedi.getStrength();
        if (str < 1.0 || str > 2.0) {
            return false;
        }
        Planet planet = getPlanet(planetName);
        if (planet == null) {
            return false;
        }
        String jediKey = jedi.getName().toLowerCase();
        // не трябва да съществува друг джедай със същото име!!!
        if (jediLocation.containsKey(jediKey)) {
            return false;
        }
        if (!planet.addJedi(jedi)) {
            return false;
        }
        jediLocation.put(jediKey, planetName.toLowerCase());
        return true;
    }

    // премахва джедай от планетата
    public boolean removeJedi(String name, String planetName) {
        Planet planet = getPlanet(planetName);
        if (planet == null) {
            return false;
        }
        boolean removed = planet.removeJedi(name);
        if (removed) {
            jediLocation.remove(name.toLowerCase());
        }
        return removed;
    }

    // повишава ранг и сила
    public boolean promoteJedi(String name, double multiplier) {
        if (multiplier <= 0) {
            return false;
        }
        String locKey = jediLocation.get(name.toLowerCase());
        if (locKey == null) {
            return false;
        }
        Planet planet = planets.get(locKey);
        for (Jedi j : planet.getJedis()) {
            if (!j.getName().equalsIgnoreCase(name)) {
                continue;
            }
            int rankIndex = j.getRank().ordinal();
            if (rankIndex >= Rank.values().length - 1) {
                return false;
            }
            // Правим повишение
            Rank nextRank = Rank.values()[rankIndex + 1];
            j.setRank(nextRank);
            double newStrength = j.getStrength() * (1 + multiplier);
            // Clamp до максимум 2.0
            if (newStrength > 2.0) {
                newStrength = 2.0;
            }
            j.setStrength(newStrength);
            return true;
        }
        return false;
    }

    // понижава ранг и сила
    public boolean demoteJedi(String name, double multiplier) {
        if (multiplier <= 0) {
            return false;
        }
        String locKey = jediLocation.get(name.toLowerCase());
        if (locKey == null) {
            return false;
        }
        Planet planet = planets.get(locKey);
        for (Jedi j : planet.getJedis()) {
            if (!j.getName().equalsIgnoreCase(name)) {
                continue;
            }
            int rankIndex = j.getRank().ordinal();
            if (rankIndex <= 0) {
                return false;
            }
            // Правим понижение
            Rank prevRank = Rank.values()[rankIndex - 1];
            j.setRank(prevRank);
            double newStrength = j.getStrength() * (1 - multiplier);
            // Clamp до минимум 1.0
            if (newStrength < 1.0) {
                newStrength = 1.0;
            }
            j.setStrength(newStrength);
            return true;
        }
        return false;
    }

    // най-силния джедай на планета
    public Jedi getStrongestJedi(String planetName) {
        Planet planet = getPlanet(planetName);
        if (planet == null || planet.getJedis().isEmpty()) {
            return null;
        }
        Jedi strongest = planet.getJedis().get(0);
        for (Jedi j : planet.getJedis()) {
            if (j.getStrength() > strongest.getStrength()) {
                strongest = j;
            }
        }
        return strongest;
    }

    // Намира най-младия джедай с определен ранг
    public Jedi getYoungestJedi(String planetName, Rank rank) {
        Planet planet = getPlanet(planetName);
        if (planet == null) {
            return null;
        }
        Jedi youngest = null;
        for (Jedi j : planet.getJedis()) {
            if (j.getRank() != rank) {
                continue;
            }
            if (youngest == null || j.getAge() < youngest.getAge() ||
                    (j.getAge() == youngest.getAge() &&
                            j.getName().compareToIgnoreCase(youngest.getName()) < 0)) {
                youngest = j;
            }
        }
        return youngest;
    }

    // най-често използвания цвят на меча за даден ранг
    public String getMostUsedSaberColor(String planetName, Rank rank) {
        Planet planet = getPlanet(planetName);
        if (planet == null) {
            return null;
        }
        Map<String, Integer> count = new HashMap<>();
        for (Jedi j : planet.getJedis()) {
            if (j.getRank() != rank) {
                continue;
            }
            String color = j.getSaberColor();
            count.put(color, count.getOrDefault(color, 0) + 1);
        }
        String most = null;
        int max = 0;
        for (Map.Entry<String, Integer> e : count.entrySet()) {
            if (e.getValue() > max) {
                most = e.getKey();
                max = e.getValue();
            }
        }
        return most;
    }

    // най-често използвания цвят на меча сред GRAND_MASTER
    public String getMostUsedSaberColor(String planetName) {
        return getMostUsedSaberColor(planetName, Rank.GRAND_MASTER);
    }

    // принтира планета и джедаите, сортирани по ранг и име
    public void printPlanet(String planetName) {
        Planet planet = getPlanet(planetName);
        if (planet == null) {
            System.out.println("Planet not found.");
            return;
        }
        System.out.println("Planet: " + planet.getName());
        List<Jedi> list = new ArrayList<>(planet.getJedis());
        Collections.sort(list, new Comparator<Jedi>() {
            @Override
            public int compare(Jedi j1, Jedi j2) {
                int cmp = j1.getRank().ordinal() - j2.getRank().ordinal();
                if (cmp != 0) {
                    return cmp;
                }
                return j1.getName().compareToIgnoreCase(j2.getName());
            }
        });
        for (Jedi j : list) {
            System.out.println("  " + j);
        }
    }

    // принтира информация за един джедай и на коя планета е
    public void printJedi(String jediName) {
        String locKey = jediLocation.get(jediName.toLowerCase());
        if (locKey == null) {
            System.out.println("Jedi not found.");
            return;
        }
        Planet planet = planets.get(locKey);
        for (Jedi j : planet.getJedis()) {
            if (j.getName().equalsIgnoreCase(jediName)) {
                System.out.println(j + " on " + planet.getName());
                return;
            }
        }
    }

    // принтира обединение на две планети с пълна информация
    public void printUnion(String planet1, String planet2) {
        Planet p1 = getPlanet(planet1);
        Planet p2 = getPlanet(planet2);
        if (p1 == null || p2 == null) {
            System.out.println("One or both planets not found.");
            return;
        }
        List<Jedi> combined = new ArrayList<>();
        combined.addAll(p1.getJedis());
        combined.addAll(p2.getJedis());
        Collections.sort(combined, new Comparator<Jedi>() {
            @Override
            public int compare(Jedi j1, Jedi j2) {
                return j1.getName().compareToIgnoreCase(j2.getName());
            }
        });
        for (Jedi j : combined) {
            String originKey = jediLocation.get(j.getName().toLowerCase());
            String origin;
            if (originKey != null && planets.containsKey(originKey)) {
                origin = planets.get(originKey).getName();
            } else {
                origin = "Unknown";
            }
            System.out.println(j + " from " + origin);
        }
    }
}
