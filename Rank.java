public enum Rank {
    YOUNGLING, INITIATE, PADAWAN, KNIGHT_ASPIRANT,
    KNIGHT, MASTER, BATTLE_MASTER, GRAND_MASTER;

    public static Rank promote(Rank rank) {
        if (rank.ordinal() < GRAND_MASTER.ordinal()) {
            return Rank.values()[rank.ordinal() + 1];
        }
        return rank;
    }

    public static Rank demote(Rank rank) {
        if (rank.ordinal() > YOUNGLING.ordinal()) {
            return Rank.values()[rank.ordinal() - 1];
        }
        return rank;
    }
}
