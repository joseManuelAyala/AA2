package kastel.model;

/**
 * A class representing a Player in the Hexagon game.
 * @author ucxug
 * @version 1.0
 */

public class Player {

    /**
     * Represents the player name.
     */
    private final String name;
    /**
     * Represents the enemy player of this player.
     */
    private  Player enemyPlayer;
    private Game currentGame;

    /**
     * Constructs a player with the specified name.
     * @param name the name of the player.
     */
    public Player(final String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     * @return thhe name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the enemy player for this player.
     * @param enemyPlayer the enemy player.
     */
    public void setEnemyPlayer(final Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    /**
     * Gets the enemy player of this player.
     * @return the enemy player of this player.
     */
    public Player getEnemyPlayer() {
        return this.enemyPlayer;
    }

    /**
     * Gets the winnin move for the player.
     * @param currentToken the game token to be checked.
     * @return the winning move Hexagon for the player.
     */
    public Hexagon getWinningMove(final GameTokens currentToken) {
        return null;
    }

    /**
     * Gets the blocking move for the player.
     * @param currentToken the game token to be checked.
     * @return the blocking move Hexagon for the player.
     */
    public Hexagon getBlockingMove(final GameTokens currentToken) {
        return null;
    }

    /**
     * Checks if the player should swap Tokens with the opponnent.
     * @return true if the player should swap , false otherwise.
     */
    public boolean isSwap() {
        return false;
    }

    /**
     * Sets the current game for the player.
     * @param game the current game.
     */
    public void setCurrentGame(final Game game) {
        this.currentGame = game;
    }

    /**
     * Gets the fourth move Hexagon for the player.
     * @return the fourth move Hexagon for the player.
     */
    public Hexagon getFourthMove() {
        return null;
    }

    /**
     * Gets the third move for the player.
     * @return the third move Hexagon for the player.
     */
    public Hexagon getThirdMove() {
        return null;
    }
}
