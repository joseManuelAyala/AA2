package kastel.model;

/**
 * An enumeration representing the tokens used in the game.
 * @author ucxug
 * @version 1.0
 */

public enum GameTokens {
    /**
     * Represents an emtpy Hexagon.
     */
    EMPTY("."),
    /**
     * Represents an Hexagon with the X Token.
     */
    X_TOKEN("X"),
    /**
     * Represents a Hexagon with the O Token.
     */
    O_TOKEN("O"),
    /**
     * Represents a Hexagon as parth of the winnig path.
     */
    WIN_TOKEN("*");

    private final String tokenRepresentation;


    /**
     * Constructs a GameTokens enum constants with the specified representation
     * @param representation the String representation of the token.
     */
    GameTokens(final String representation) {
        this.tokenRepresentation = representation;
    }

    /**
     * Gets the representation of the token.
     * @return the String representation of the token.
     */
    public String getTokenRepresentation() {
        return this.tokenRepresentation;
    }

    /**
     * Gets the rival token for the current token.
     * The empty Token does not have a rival token.
     * @return the rival token for the current token.
     */
    public GameTokens getRivalToken() {
        if (this == X_TOKEN) {
            return O_TOKEN;
        }
        return X_TOKEN;
    }
}
