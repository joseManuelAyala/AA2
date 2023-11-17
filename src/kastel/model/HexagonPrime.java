package kastel.model;

import java.util.LinkedList;
import java.util.List;

/**
 * A class representing the Hexagon Prime game.
 * @author ucxug
 * @version 1.0
 */

public class HexagonPrime {

    /**
     * Stores a list of Hexagon game instances.
     */
    private final List<Game> hexagonGames;
    /**
     * Specifies the board size used in the game instances.
     */
    private final int boardSize;
    /**
     * Represents the current player taking their turn.
     */
    private Player currentPlayer;
    /**
     * Represents the first player participating in the game.
     */
    private final Player playerOne;
    /**
     * Represents the second player participating in the game.
     */
    private final Player playerTwo;
    /**
     * Represents the current game being played.
     */
    private Game currentGame;


    /**
     * Constructs a HexagonPrime game with the specified parameters.
     * @param boardSize the size of the board game for all Hexagon Games.
     * @param playerOne the first Player.
     * @param playerTwo the second Player.
     */
    public HexagonPrime(final int boardSize, final Player playerOne, final Player playerTwo) {
        this.currentGame = new Game("Prime", playerOne, playerTwo, new GameBoard(boardSize));
        //Sets the principal game name to Prime.
        this.boardSize = boardSize;
        this.hexagonGames = new LinkedList<>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOne.setEnemyPlayer(this.playerTwo);
        this.playerTwo.setEnemyPlayer(this.playerOne);
        this.currentPlayer = this.playerOne;
        this.hexagonGames.add(this.currentGame);

    }

    /**
     * changes the current game.
     * @param game the new current game.
     */
    public void changeGame(final Game game) {
        this.currentGame = game;
    }

    /**
     * Retrieves the game by its name.
     * @param name the name of the game to retrieve.
     * @return the game with the specified name, or null if the game name is not found.
     */
    public Game getGame(final String name) {
        for (Game game : this.hexagonGames) {
            if (game.getGameName().equals(name)) {
                //If the game name matches the given name it will be returned.
                return game;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of all games.
     * @return A list of all games.
     */
    public List<Game> getGames() {
        return new LinkedList<>(this.hexagonGames);
    }

    /**
     * Add a new game with the given name.
     * @param gameName the name of the new game.
     */
    public void addNewGame(final String gameName) {
        if (this.currentGame.isGameOver(true)) {
            //Adds a new game setting the winning player as the second player.
            Game newGame = new Game(gameName, this.currentPlayer, this.currentPlayer.getEnemyPlayer(),
                new GameBoard(this.boardSize));
            this.currentGame = newGame;
            this.hexagonGames.add(newGame);
        }
        //Adds a new game and sets it as the current game.
        Game newGame = new Game(gameName, this.playerOne, this.playerTwo,
            new GameBoard(this.boardSize));
        this.currentGame = newGame;
        this.hexagonGames.add(newGame);

    }

    /**
     * Retrieves the current player.
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Retrieves the current game.
     * @return the current game.
     */
    public Game getCurrentGame() {
        return this.currentGame;
    }

    /**
     * Sets the game token on a specified hexagon.
     * @param hexagon the hexagon wheret the token will be seted.
     */
    public void setGameToken(final Hexagon hexagon) {
        this.currentGame.getGameBoard().setToken(hexagon,
            this.currentGame.getPlayerToken(currentGame.getCurrentPlayer()));
        //The hexagon will also be seted in the copy of the game board.
        this.currentGame.getWinningBoard().setToken(hexagon.getCopy(),
            this.currentGame.getPlayerToken(currentGame.getCurrentPlayer()));
        this.currentGame.addMovement(hexagon);
        this.currentGame.addTurn();
        changeCurrentPlayer();
    }

    /**
     * Changes the current player to the enemy player.
     */
    public void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer.getEnemyPlayer();
        //The current player will be set as the enemy player of the last current player.
        this.currentGame.setCurrentPlayer(this.currentGame.getCurrentPlayer());
    }
}
