public class Player {
    /** Name of this player. */
    private static String name;

    /** Create a new player with the given name. */
    public Player(String name) {
        this.name = name;
    }

    /** Return the name of this player. */
    public static String name() {
        return name;
    }
}
