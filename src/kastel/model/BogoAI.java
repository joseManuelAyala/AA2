package kastel.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class representing the BogoAI Artificial player that employes a basic strategy for the HexagonPrime game.
 * @author ucxug
 * @version 1.0
 */

public class BogoAI extends Player {
    private static final int EVEN_DIVISOR = 2;
    private static final int FIRST_MOVEMENT_INDEX = 0;
    private static final int EVEN_COORDINATES = 0;
    private static final int EXPETEC_MOVEMENTS_SIZE = 1;
    /**
     * The current game that is being played.
     */
    private Game currentGame;

    /**
     * Constructs a new instance of the BogoAI player.
     */
    public BogoAI() {
        super(AIPlayers.BogoAI.getPlayerName());
    }

    @Override
    public Hexagon getWinningMove(GameTokens currentToken) {
        for (Hexagon[] hexagons : currentGame.getGameBoard().getBoard()) {
            for (Hexagon winningHexagon : hexagons) {
                if (winningHexagon.getContent() == GameTokens.EMPTY) {
                    //Places the hexagon and checks if the game is over.
                    currentGame.getGameBoard().setToken(winningHexagon, currentToken);
                    if (this.currentGame.isGameOver(false)) {
                        currentGame.getGameBoard().setToken(winningHexagon, GameTokens.EMPTY);
                        return winningHexagon;
                    }
                    //Sets the hexagons back to their basic content.
                    currentGame.getGameBoard().setToken(winningHexagon, GameTokens.EMPTY);
                }
            }
        }
        return null;
    }

    /**
     * Determines a list of possible blocking moves.
     * @param currentToken the current token to be checked.
     * @return A list of hexagons representing the potential blocking moves.
     */
    private List<Hexagon> getMultipleBlockingMoves(final GameTokens currentToken) {
        List<Hexagon> hexagons = new LinkedList<>();
        for (Hexagon[] blocckingHexagon : currentGame.getGameBoard().getBoard()) {
            for (Hexagon hexagon : blocckingHexagon) {
                if (hexagon.getContent() == GameTokens.EMPTY) {
                    currentGame.getGameBoard().setToken(hexagon, currentToken);
                    //Places the hexagon and checks if the game is over.
                    if (this.currentGame.isGameOver(false)) {
                        currentGame.getGameBoard().setToken(hexagon, GameTokens.EMPTY);
                        hexagons.add(hexagon);
                    }
                    //Sets the hexagon back to their basis content.
                    currentGame.getGameBoard().setToken(hexagon, GameTokens.EMPTY);
                }
            }
        }
        return hexagons;
    }

    @Override
    public Hexagon getBlockingMove(final GameTokens currentToken) {
        List<Hexagon> blockMoves = getMultipleBlockingMoves(currentToken);
        if (blockMoves.isEmpty()) {
            return null;
        }
        Hexagon hexagon = blockMoves.get(FIRST_MOVEMENT_INDEX);
        //Returns the blocking hexagon following the given conditions.
        for (Hexagon hexagonToPlace : blockMoves) {
            if (hexagonToPlace.getxCoordinate() < hexagon.getxCoordinate()
                || hexagonToPlace.getxCoordinate() == hexagon.getxCoordinate()
                && hexagonToPlace.getyCoordinate() < hexagon.getyCoordinate()) {
                hexagon = hexagonToPlace;
            }
        }
        return hexagon;
    }

    @Override
    public void setCurrentGame(final Game game) {
        //Sets the current game that is being played so that BogoAI can implent his Strategy movements.
        this.currentGame = game;
    }

    @Override
    public boolean isSwap() {
        if (this.currentGame.getGameMoves().size() != EXPETEC_MOVEMENTS_SIZE) {
            return false;
        }
        if (this.currentGame.getLastMove().containsKey(this)) {
            //If the player already placed a token, the swap is not possible.
            return false;
        }
        //The sum of the x and y coordinate must be a even number so that the GameTokens can be swapped.
        Hexagon hexagon = this.currentGame.getLastMove().get(this.getEnemyPlayer());
        return (hexagon.getyCoordinate() + hexagon.getxCoordinate()) % EVEN_DIVISOR == EVEN_COORDINATES;
    }

    @Override
    public Hexagon getFourthMove() {
        if (getSymmetricHexagon() != null) {
            //Search for the symetric Hexagon.
            return getSymmetricHexagon();
        }
        //If the symetric hexagon is not found the first empty hexagon will be returned.
        for (int i = 0; i <  currentGame.getGameBoard().getBoardSize(); i++) {
            for (int j = 0; j < currentGame.getGameBoard().getBoardSize(); j++) {
                if (currentGame.getGameBoard().getHexagon(j, i).getContent() == GameTokens.EMPTY) {
                    //The first empty hexagon will be returned.
                    return currentGame.getGameBoard().getHexagon(j, i);
                }
            }
        }
        return null;
    }

    /**
     * Returns the symetric hexagon to the last placed hexagon in the game board.
     * @return the symetric hexagon to the last placed hexagon if the hexagon is empty, null otherwise.
     */
    private Hexagon getSymmetricHexagon() {
        //Checks the middle Hexagon of the game board.
        int midddleIndex = (this.currentGame.getGameBoard().getBoardSize() - 1) / EVEN_DIVISOR;
        if (this.currentGame.getLastMove() == null) {
            return null;
        }
        Map<Player, Hexagon> gameLastMove = this.currentGame.getLastMove();
        Hexagon lastPlacedHexagon = gameLastMove.get(gameLastMove.keySet().iterator().next());
        //Checks if the symetric Hexagon to the last placed Hexagon is free.
        int xCoordinate = midddleIndex + (midddleIndex - lastPlacedHexagon.getxCoordinate());
        int yCoordinate = midddleIndex + (midddleIndex - lastPlacedHexagon.getyCoordinate());
        Hexagon toPlaceHexagon = this.currentGame.getGameBoard().getHexagon(xCoordinate, yCoordinate);
        if (toPlaceHexagon.getContent() == GameTokens.EMPTY) {
            return toPlaceHexagon;
        }
        return null;
    }
}
