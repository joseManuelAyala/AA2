package kastel.model;

/**
 * An Enum representing the available Artificial Players type for Hexagon Prime.
 * @author ucxug
 * @version 1.0
 */

public enum AIPlayers {
    /**
     * BogoAI player Type.
     */
    BogoAI("BogoAI"),
    /**
     * HeroAI player Type.
     */
    HeroAI("HeroAI");
    private final String playerName;

    /**
     * Constructor for AIPlayer enum.
     * @param name the name of the AI player type.
     */
    AIPlayers(final String name) {
        this.playerName = name;
    }

    /**
     * Get the name of the AI player type.
     * @return the name of the AI player type.
     */
    public String getPlayerName() {
        return this.playerName;
    }
}
