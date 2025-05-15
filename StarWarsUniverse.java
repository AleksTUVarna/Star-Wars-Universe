package commands;

import java.io.Serializable;
import java.util.*;
import models.Jedi;
import models.Planet;
import models.Rank;

public class StarWarsUniverse implements Serializable {
    // helper: retrieve a planet by name (case-insensitive)
    public models.Planet getPlanet(String name) {
        if (name == null) return null;
        return planets.get(name.toLowerCase());
    }
    private Map<String, Planet> planets = new HashMap<>();
    private Map<String, String> jediLocation = new HashMap<>();

    public boolean addPlanet(String name) {
        String key = name.toLowerCase();
        if (planets.containsKey(key)) return false;
        planets.put(key, new Planet(name));
        return true;
    }

    public boolean createJedi(String planetName, Jedi jedi) {
        String pkey = planetName.toLowerCase();
        if (!planets.containsKey(pkey)) return false;
        if (jediLocation.containsKey(jedi.getName().toLowerCase())) return false;
        Planet p = planets.get(pkey);
        if (p.addJedi(jedi)) {
            jediLocation.put(jedi.getName().toLowerCase(), pkey);
            return true;
        }
        return false;
    }

    public boolean removeJedi(String name, String planetName) {
        String pkey = planetName.toLowerCase();
        Planet p = planets.get(pkey);
        if (p == null) return false;
        if (p.removeJedi(name)) {
            jediLocation.remove(name.toLowerCase());
            return true;
        }
        return false;
    }

    public boolean promoteJedi(String name, double mult) {
        if (mult <= 0) return false;
        String key = name.toLowerCase();
        String pkey = jediLocation.get(key);
        if (pkey == null) return false;
        Planet p = planets.get(pkey);
        for (Jedi j : p.getJedis()) {
            if (j.getName().equalsIgnoreCase(name)) {
                int idx = j.getRank().ordinal();
                if (idx < Rank.values().length - 1) {
                    Rank next = Rank.values()[idx+1];
                    j.setRank(next);
                    j.setStrength(j.getStrength() * (1 + mult));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean demoteJedi(String name, double mult) {
        if (mult <= 0) return false;
        String key = name.toLowerCase();
        String pkey = jediLocation.get(key);
        if (pkey == null) return false;
        Planet p = planets.get(pkey);
        for (Jedi j : p.getJedis()) {
            if (j.getName().equalsIgnoreCase(name)) {
                int idx = j.getRank().ordinal();
                if (idx > 0) {
                    Rank prev = Rank.values()[idx-1];
                    j.setRank(prev);
                    double newStr = j.getStrength() * (1 - mult);
                    j.setStrength(newStr < 0 ? 0 : newStr);
                    return true;
                }
            }
        }
        return false;
    }

    public Jedi getStrongestJedi(String planetName) {
        Planet p = planets.get(planetName.toLowerCase());
        if (p == null || p.getJedis().isEmpty()) return null;
        Jedi best = null;
        for (Jedi j : p.getJedis()) {
            if (best == null || j.getStrength() > best.getStrength()) best = j;
        }
        return best;
    }

    public Jedi getYoungestJedi(String planetName, Rank rnk) {
        Planet p = planets.get(planetName.toLowerCase());
        if (p == null) return null;
        Jedi youngest = null;
        for (Jedi j : p.getJedis()) {
            if (j.getRank() == rnk) {
                if (youngest == null || j.getAge() < youngest.getAge() ||
                        (j.getAge() == youngest.getAge() && j.getName().compareToIgnoreCase(youngest.getName()) < 0)) {
                    youngest = j;
                }
            }
        }
        return youngest;
    }

    public String getMostUsedSaberColor(String planetName, Rank rnk) {
        Planet p = planets.get(planetName.toLowerCase());
        if (p == null) return null;
        Map<String,Integer> count = new HashMap<>();
        for (Jedi j : p.getJedis()) {
            if (j.getRank() == rnk) {
                String c = j.getSaberColor().toLowerCase();
                count.put(c, count.getOrDefault(c,0)+1);
            }
        }
        String best = null; int max=0;
        for (Map.Entry<String,Integer> e : count.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        }
        return best;
    }

    public String getMostUsedSaberColor(String planetName) {
        Planet p = planets.get(planetName.toLowerCase());
        if (p == null) return null;
        Map<String,Integer> count = new HashMap<>();
        for (Jedi j : p.getJedis()) {
            if (j.getRank() == Rank.GRAND_MASTER) {
                String c = j.getSaberColor().toLowerCase();
                count.put(c, count.getOrDefault(c,0)+1);
            }
        }
        String best = null; int max=0;
        for (Map.Entry<String,Integer> e : count.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        }
        return best;
    }

    public void printPlanet(String planetName) {
        Planet p = planets.get(planetName.toLowerCase());
        if (p == null) {
            System.out.println("Planet not found."); return;
        }
        System.out.println("Planet: " + p.getName());
        for (Jedi j : p.getJedis()) System.out.println("  " + j);
    }

    public void printJedi(String jediName) {
        String key = jediName.toLowerCase();
        String pkey = jediLocation.get(key);
        if (pkey == null) { System.out.println("Jedi not found."); return; }
        Planet p = planets.get(pkey);
        for (Jedi j : p.getJedis()) {
            if (j.getName().equalsIgnoreCase(jediName)) {
                System.out.println(j + " on " + p.getName()); return;
            }
        }
    }

    public void printUnion(String p1, String p2) {
        Planet a = planets.get(p1.toLowerCase());
        Planet b = planets.get(p2.toLowerCase());
        if (a == null || b == null) {
            System.out.println("One or both planets not found.");
            return;
        }
        List<Jedi> combined = new ArrayList<>();
        combined.addAll(a.getJedis());
        combined.addAll(b.getJedis());
        combined.sort(Comparator.comparing(Jedi::getName, String.CASE_INSENSITIVE_ORDER));
        for (Jedi j : combined) {
            String loc = jediLocation.get(j.getName().toLowerCase());
            String original = planets.get(loc).getName();
            System.out.println(j.getName() + " (from " + original + ")");
        }
    }
}