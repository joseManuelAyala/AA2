package kastel.model;

/**
 * A manager class responsible for managing the interaction and desicions of an artificial player in a hexagon game.
 * @author ucxug
 * @version 1.0
 */
public class ArtificialManager {
    /**
     * The current artifial player of the ArtificialManager instance.
     */
    private final Player artificialPlayer;

    /**
     * The current game that is being played.
     */
    private Game currentGame;

    /**
     * Constructs an ArtificialManager with the specified artificial player.
     * @param artificialPlayer the artificial player to manage.
     */
    public ArtificialManager(final Player artificialPlayer) {
        this.artificialPlayer = artificialPlayer;
    }

    /**
     * Retrieves a move for the artificial player based on various strategies.
     * @return the move result, wich may include a move or a swap indication.
     */
    public MoveResult getArtificialMove() {

        Hexagon hexagonToPlace = this.artificialPlayer.getWinningMove(currentGame.getPlayerToken(artificialPlayer));
        //Checks if there is a posible winning move.

        if (hexagonToPlace != null) {
            return new MoveResult(hexagonToPlace, false);
        }

        hexagonToPlace = this.artificialPlayer.getBlockingMove((
            currentGame.getPlayerToken(artificialPlayer).getRivalToken()));
        //Checks if there is a posible blocking move.
        if (hexagonToPlace != null) {
            return new MoveResult(hexagonToPlace, false);
        }
        if (this.artificialPlayer.getName().equals(AIPlayers.BogoAI.getPlayerName())
            //Checks if the players swaps the game tokens.
            && this.artificialPlayer.isSwap()) {
            return new MoveResult(null, true);
        }
        hexagonToPlace = this.artificialPlayer.getThirdMove();
        //Checks the third condition for the posible move.
        if (hexagonToPlace != null) {
            return new MoveResult(hexagonToPlace, false);
        }
        //If all of the above conditions were null, the fourth move condition will be returned.
        return new MoveResult(this.artificialPlayer.getFourthMove(), false);

    }

    /**
     * Gets the artificial player managed by this Artificial manager.
     * @return the artificial player.
     */
    public Player getArtificialPlayer() {
        return this.artificialPlayer;
    }

    /**
     * Sets the current game that the artificial player is playing.
     * @param game the current game instance.
     */
    public void setCurrentGame(final Game game) {
        this.currentGame = game;
        this.artificialPlayer.setCurrentGame(game);
    }

}
