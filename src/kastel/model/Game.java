package kastel.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kastel.model.searchalgorithms.BreadthFirstSearch;

/**
 * This class represents a HexagonPrime Game.
 * This class is responsible for managing the turns , the players and also to store the players moves.
 * @author ucxug
 * @version 1.0
 */

public class Game {
    /**
     * Stores a list of game moves made by players.
     */
    private final List<Map<Player, Hexagon>> gameMoves;

    /**
     * Maps players to their corresponding game token used in this game.
     */
    private final Map<Player, GameTokens> playerGameTokens;
    /**
     * Stores the name of the game.
     */
    private final String gameName;
    /**
     * Indicates wheter the game is over.
     */
    private boolean gameOver;
    /**
     * Represents the first player participating in the game.
     */
    private final Player playerOne;
    /**
     * Represents the second player participating in this game.
     */
    private final Player playerTwo;
    /**
     * Represents the game board associated with this game.
     */
    private final GameBoard gameBoard;
    /**
     * Stores a copy of the game board , so that when the game is over the copy will be returned.
     */
    private final GameBoard winningBoard;
    private int turnsCount;
    private Player currentPlayer;

    /**
     * Constructs a new game instance.
     * @param name the name of the game.
     * @param playerOne the first player in turn.
     * @param playerTwo the second player in turn.
     * @param board the ganme board.
     */
    public Game(final String name, final Player playerOne, final Player playerTwo, final GameBoard board) {
        this.gameName = name;
        this.playerGameTokens = new HashMap<>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.turnsCount = 0;
        this.gameMoves = new LinkedList<>();
        this.gameBoard = board;
        this.winningBoard = new GameBoard(board.getBoardSize());
        this.currentPlayer = playerOne;
        playerGameTokens.put(this.playerOne, GameTokens.X_TOKEN);
        playerGameTokens.put(this.playerTwo, GameTokens.O_TOKEN);
    }

    /**
     * Retrives the name of the game.
     * @return the name of the game.
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * Retrieves the game board.
     * @return the game board.
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Swaps thhe game tokens of the players.
     */
    public void swapMovement() {
        this.playerGameTokens.clear();
        this.playerGameTokens.put(this.playerOne, GameTokens.O_TOKEN);
        this.playerGameTokens.put(this.playerTwo, GameTokens.X_TOKEN);
        addTurn();
        changeCurrentPlayer();

        //Changes the firs placed moved for both players.
        Map<Player, Hexagon> firstMove = getLastMove();
        Map<Player, Hexagon> newFirstmove = new HashMap<>();
        Player player = firstMove.keySet().iterator().next();
        newFirstmove.put(player.getEnemyPlayer(), firstMove.get(player));
        this.gameMoves.clear();
        this.gameMoves.add(newFirstmove);
    }


    /**
     * Checks if the game is over.
     * @param markWinningHexagons Indicates whether te winning path needs to be marked.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver(boolean markWinningHexagons) {
        if (markWinningHexagons) {
            didOWin(true);
            didXWin(true);
            return this.gameOver;
        }
        return getTurnsCount() >= this.gameBoard.getBoardSize() && (didOWin(false)
            || didXWin(false));

    }


    private boolean didOWin(boolean markWinningHexagons) {
        if (getTurnsCount() < this.gameBoard.getBoardSize()) {
            return false;
        }
        for (int i = 0; i < this.gameBoard.getBoardSize(); i++) {
            for (int j = 0; j < this.gameBoard.getBoardSize(); j++) {
                //Tries to connect the the west side to the east side.
                boolean validateHexagons = matchesHexagonContent(this.gameBoard.getHexagon(0, i),
                    GameTokens.O_TOKEN)
                    && matchesHexagonContent(this.gameBoard.getHexagon(
                        this.gameBoard.getBoardSize() - 1, j), GameTokens.O_TOKEN);
                if (validateHexagons
                    && getWinningPath(this.gameBoard.getHexagon(0, i),
                    this.gameBoard.getHexagon(this.gameBoard.getBoardSize() - 1, j),
                    GameTokens.O_TOKEN, markWinningHexagons)) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean didXWin(boolean markWinningHexagons) {
        if (getTurnsCount() < this.gameBoard.getBoardSize()) {
            //If the total movements are less than the game board size , no player has won and false will be returned.
            return false;
        }
        for (int i = 0; i < this.gameBoard.getBoardSize(); i++) {
            for (int j = 0; j < this.gameBoard.getBoardSize(); j++) {
                //Tries to connect the north side with the south side.
                boolean validateHexagons = matchesHexagonContent(this.gameBoard.getHexagon(i,
                    0), GameTokens.X_TOKEN)
                    && matchesHexagonContent(this.gameBoard.getHexagon(j,
                    this.gameBoard.getBoardSize() - 1), GameTokens.X_TOKEN);
                //Both Hexagons content must match the given content.
                if (validateHexagons
                    && getWinningPath(this.gameBoard.getHexagon(i, 0),
                    this.gameBoard.getHexagon(j, this.gameBoard.getBoardSize() - 1),
                    GameTokens.X_TOKEN, markWinningHexagons)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesHexagonContent(final Hexagon hexagon, final GameTokens token) {
        return hexagon.getContent() == token;
    }

    /**
     * Retrives the game token associated with a player.
     * @param player the player.
     * @return the game token associated with the player.
     */
    public GameTokens getPlayerToken(final Player player) {
        for (Player gamePlayer : this.playerGameTokens.keySet()) {
            if (gamePlayer.getName().equals(player.getName())) {
                return this.playerGameTokens.get(gamePlayer);
            }
        }
        return null;
    }

    /**
     * Retrieves a list of game moves, each containin player and hexagon information.
     * @return a list of game moves.
     */
    public  List<Map<Player, Hexagon>> getGameMoves() {
        return new LinkedList<>(this.gameMoves);
    }

    /**
     * Adds a players movement to the list of game moves.
     * @param hexagon the hexagon representing the move.
     */
    public void addMovement(final Hexagon hexagon) {
        //Adds a new movements to the game board and stores the movement information.
        Map<Player, Hexagon> newMovement = new HashMap<>();
        newMovement.put(this.currentPlayer, hexagon);
        this.gameMoves.add(newMovement);
        //Changes the current player.
        changeCurrentPlayer();
    }

    /**
     * Retrieves the number of turns that have been played in the game.
     * @return the numbers of turns played.
     */
    public int getTurnsCount() {
        return this.turnsCount;
    }

    /**
     * Increases the turn count by one.
     */
    public void addTurn() {
        this.turnsCount++;
    }


    private boolean getWinningPath(final Hexagon start, final Hexagon end, final GameTokens token,
                                   final boolean markHexagons) {
        List<Hexagon> winningPath =  BreadthFirstSearch.breadthFirstSearch(this, start, end, token);
        //Gets the posible winning path with the help from breadth First Search algorithm.
        if (winningPath.isEmpty()) {
            //If an empty list is returned it knows that a posible path does not exist and returns false.
            return false;
        }
        if (markHexagons) {
            // If markHexagons,  all the winning path hexagons will be marked with the winning game token.
            this.winningBoard.markWinningHexagons(winningPath);
            this.winningBoard.markWinningPath(token);
            //The game will be over.
            this.gameOver = true;
        }
        return true;
    }


    /**
     * Returns a copy of the current game winning board.
     * @return the winning board.
     */
    public GameBoard getWinningBoard() {
        return winningBoard;
    }

    /**
     * Retrieves the last move of the game.
     * @return the last move of the game if the movements count is not equal to 0,
     *      null otherwise.
     */
    public Map<Player, Hexagon> getLastMove() {
        if (this.gameMoves.isEmpty()) {
            return null;
        } else  {
            return new HashMap<>(this.gameMoves.get(this.gameMoves.size() - 1));
        }
    }

    /**
     * Sets the current player for the game.
     * @param player the current player to be set.
     */
    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    private void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer.getEnemyPlayer();
    }

    /**
     * Returns the current player of the game.
     * @return the current player of the game.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Returns the Player last game move.
     * @param player the player to check the move for.
     * @return the last placed Hexagon if found, null otherwise.
     */
    public Hexagon getPlayerLastMove(final Player player) {
        Hexagon lastMove = null;
        for (int i = 0; i < getGameMoves().size(); i++) {
            Map<Player, Hexagon> movement = getGameMoves().get(i);
            if (movement.containsKey(player)) {
                lastMove = movement.get(player);
            }
        }
        return lastMove;
    }

}
