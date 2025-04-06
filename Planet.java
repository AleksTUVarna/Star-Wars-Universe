import java.util.*;
import java.util.stream.Collectors;

public class Planet {
    private String name;
    private List<Jedi> jedis;

    public Planet(String name) {
        this.name = name;
        this.jedis = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Jedi> getJedis() {
        return jedis;
    }

    public boolean addJedi(Jedi jedi) {
        for (Jedi j : jedis) {
            if (j.getName().equals(jedi.getName())) {
                return false;
            }
        }
        jedis.add(jedi);
        return true;
    }

    public boolean removeJedi(String name) {
        for (int i = 0; i < jedis.size(); i++) {
            if (jedis.get(i).getName().equals(name)) {
                jedis.remove(i);
                return true;
            }
        }
        return false;
    }

    public Jedi getStrongestJedi() {
        if (jedis.isEmpty()) return null;
        Jedi strongest = jedis.get(0);
        for (Jedi j : jedis) {
            if (j.getStrength() > strongest.getStrength()) {
                strongest = j;
            }
        }
        return strongest;
    }

    public Jedi getYoungestJediByRank(Rank rank) {
        Jedi youngest = null;
        for (Jedi j : jedis) {
            if (j.getRank() == rank) {
                if (youngest == null || j.getAge() < youngest.getAge() ||
                        (j.getAge() == youngest.getAge() && j.getName().compareTo(youngest.getName()) < 0)) {
                    youngest = j;
                }
            }
        }
        return youngest;
    }

    public String getMostUsedColorByRank(Rank rank) {
        Map<String, Integer> colorCount = new HashMap<>();
        for (Jedi j : jedis) {
            if (j.getRank() == rank) {
                colorCount.put(j.getSaberColor(), colorCount.getOrDefault(j.getSaberColor(), 0) + 1);
            }
        }
        String mostUsed = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostUsed = entry.getKey();
            }
        }
        return mostUsed != null ? mostUsed : "No sabers found";
    }

    public String getMostUsedColorByGrandMasters() {
        Map<String, Integer> colorCount = new HashMap<>();
        for (Jedi j : jedis) {
            if (j.getRank() == Rank.GRAND_MASTER) {
                colorCount.put(j.getSaberColor(), colorCount.getOrDefault(j.getSaberColor(), 0) + 1);
            }
        }
        String mostUsed = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostUsed = entry.getKey();
            }
        }
        return mostUsed != null ? mostUsed : "No sabers found";
    }

    public void printSortedJedis() {
        jedis.sort(new Comparator<Jedi>() {
            @Override
            public int compare(Jedi a, Jedi b) {
                int rankCompare = Integer.compare(a.getRank().ordinal(), b.getRank().ordinal());
                if (rankCompare != 0) return rankCompare;
                return a.getName().compareTo(b.getName());
            }
        });
        for (Jedi j : jedis) {
            System.out.println(j);
        }
    }

    public String toDataString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jedis.size(); i++) {
            sb.append(jedis.get(i).toDataString());
            if (i != jedis.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void loadFromDataStrings(List<String> dataLines) {
        for (String line : dataLines) {
            jedis.add(Jedi.fromDataString(line));
        }
    }
}