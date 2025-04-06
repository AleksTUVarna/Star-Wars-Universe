public class Jedi {
    private String name;
    private Rank rank;
    private int age;
    private String saberColor;
    private double strength;

    public Jedi(String name, Rank rank, int age, String saberColor, double strength) {
        this.name = name;
        this.rank = rank;
        this.age = age;
        this.saberColor = saberColor;
        this.strength = strength;
    }

    public void promote(double multiplier) {
        if (rank != Rank.GRAND_MASTER && multiplier > 0) {
            rank = Rank.promote(rank);
            strength += multiplier * strength;
        }
    }

    public void demote(double multiplier) {
        if (rank != Rank.YOUNGLING && multiplier > 0) {
            rank = Rank.demote(rank);
            strength -= multiplier * strength;
        }
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return rank;
    }

    public int getAge() {
        return age;
    }

    public String getSaberColor() {
        return saberColor;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Rank: " + rank.name() + "\n" +
                "Age: " + age + "\n" +
                "Saber Color: " + saberColor + "\n" +
                "Strength: " + String.format("%.2f", strength);
    }

    public String toDataString() {
        return name + "|" + rank.name() + "|" + age + "|" + saberColor + "|" + strength;
    }

    public static Jedi fromDataString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length != 5) throw new IllegalArgumentException("Invalid Jedi data: " + data);
        String name = parts[0];
        Rank rank = Rank.valueOf(parts[1]);
        int age = Integer.parseInt(parts[2]);
        String saberColor = parts[3];
        double strength = Double.parseDouble(parts[4]);
        return new Jedi(name, rank, age, saberColor, strength);
    }
}

