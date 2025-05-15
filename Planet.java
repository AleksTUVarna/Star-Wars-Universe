package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planet implements Serializable {
    private String name;
    private List<Jedi> jedis;

    public Planet(String name) {
        this.name = name;
        this.jedis = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Jedi> getJedis() { return jedis; }

    public boolean addJedi(Jedi jedi) {
        for (Jedi j : jedis) {
            if (j.getName().equalsIgnoreCase(jedi.getName())) {
                return false;
            }
        }
        jedis.add(jedi);
        return true;
    }

    public boolean removeJedi(String name) {
        return jedis.removeIf(j -> j.getName().equalsIgnoreCase(name));
    }
}
