package kastel.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kastel.model.searchalgorithms.Dijkstra;
/**
 * This class represents the AI Player HeroAI that extends the class Player.
 * This AI plyae is designed to make strategic moves in the game.
 * @author ucxug
 * @version 1.0
 *
 */
public class HeroAI extends Player {
    private static final int FIRST_HEXAGON_INDEX = 0;

    /**
     * The current game that is being played.
     */
    private Game currentGame;

    /**
     * Constructs a new HeroAI player.
     */
    public HeroAI() {
        super(AIPlayers.HeroAI.getPlayerName());
    }

    /**
     * This method attempts to find a winning move on the game board for the AI player.
     * @return the winning move hexagon, or null if no winning move is found.
     */
    @Override
    public Hexagon getWinningMove(GameTokens currentToken) {
        for (Hexagon[] boardRowHexagons : currentGame.getGameBoard().getBoard()) {
            for (Hexagon hexagon : boardRowHexagons) {
                if (hexagon.getContent() == GameTokens.EMPTY) {
                    //Places the hexagon with the given token and checks if the game is over.
                    currentGame.getGameBoard().setToken(hexagon, currentToken);
                    if (this.currentGame.isGameOver(false)) {
                        currentGame.getGameBoard().setToken(hexagon, GameTokens.EMPTY);
                        return hexagon;
                    }
                    //Sets the hexagon content bakc to basic state.
                    currentGame.getGameBoard().setToken(hexagon, GameTokens.EMPTY);
                }
            }
        }
        return null;
    }

    @Override
    public Hexagon getBlockingMove(final GameTokens currentToken) {
        List<Hexagon> blockMoves = getMultipleBlockingMoves();
        if (blockMoves.isEmpty()) {
            return null;
        }
        Hexagon hexagonToBlock = blockMoves.get(FIRST_HEXAGON_INDEX);
        for (Hexagon hexagon : blockMoves) {
            //Searchs for the blocking hexagon with the given specifications.
            if (hexagon.getxCoordinate() < hexagonToBlock.getxCoordinate()
                || hexagon.getxCoordinate() == hexagonToBlock.getxCoordinate()
                && hexagon.getyCoordinate() < hexagonToBlock.getyCoordinate()) {
                hexagonToBlock = hexagon;
            }
        }
        return hexagonToBlock;
    }

    private List<Hexagon> getMultipleBlockingMoves() {
        List<Hexagon> blockingMoves = new LinkedList<>();
        for (Hexagon[] boardRow : this.currentGame.getGameBoard().getBoard()) {
            for (Hexagon blockingHexagon : boardRow) {
                if (blockingHexagon.getContent() == GameTokens.EMPTY) {
                    //Sets the hexagon with the given content and checks if the game is over.
                    currentGame.getGameBoard().setToken(blockingHexagon, GameTokens.X_TOKEN);
                    if (this.currentGame.isGameOver(false)) {
                        currentGame.getGameBoard().setToken(blockingHexagon, GameTokens.EMPTY);
                        //Adds the blocking hexagon to the list to be returned.
                        blockingMoves.add(blockingHexagon);
                    }
                    //Sets the hexagon back to the basic state.
                    currentGame.getGameBoard().setToken(blockingHexagon, GameTokens.EMPTY);
                }
            }
        }
        return blockingMoves;
    }

    /**
     * Sets the current game instances for the AI player.
     * @param game the current game.
     */
    @Override
    public void setCurrentGame(final Game game) {
        this.currentGame = game;
    }



    private boolean matchesHexagonContent(final Hexagon hexagon) {
        return hexagon.getContent() == GameTokens.EMPTY;
    }

    /**
     * Implements a strategic move for the AIs third move.
     * @return the selected hexagon for the third move.
     */
    @Override
    public Hexagon getThirdMove() {
        //Searchs for the last placed heroAI hexagon.
        Hexagon lastMove = this.currentGame.getPlayerLastMove(this);
        if (lastMove != null) {
            return null;
        }
        return getFirstFreeHexagon();
    }

    private int getBoardSize() {
        return this.currentGame.getGameBoard().getBoardSize();
    }

    /**
     * Returns the first free hexagon in the game board.
     * @return the first free hexagon if found, null otherwise.
     */
    private Hexagon getFirstFreeHexagon() {
        //Searchs for the lowest x and y coordinate hexagon.
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                Hexagon firstFreeHexagon = this.currentGame.getGameBoard().getHexagon(i, j);
                if (matchesHexagonContent(firstFreeHexagon)) {
                    //The hexagon must be empty to be returned.
                    return firstFreeHexagon;
                }
            }
        }
        return null;
    }

    /**
     * Builds a posible token path to the east side of the game board.
     * @param start the start hexagon from the path.
     * @param end the end hexagon from the path.
     * @param token the token so search for each hexagon of the path.
     * @return a list of hexagons forming the path if found, or a empty list if no path is found.
     */
    private List<Hexagon> getPathToEastSide(final Hexagon start, final Hexagon end) {
        /*Tries to find a posible path starting with the start hexagon and ending with the end hexagon.
         Tries to connect the start hexagon with the east side. */

        Map<Hexagon, Hexagon> parentMap
            = Dijkstra.dijkstraWithPredecessors(this.currentGame.getGameBoard(), end, start, GameTokens.EMPTY);
        List<Hexagon>  path = buildShortestPath(end, parentMap);
        if (path.isEmpty() || !path.get(FIRST_HEXAGON_INDEX).equals(start) || !path.get(path.size() - 1).equals(end)) {
            return new LinkedList<>();
        }
        return path;

    }

    private List<Hexagon> buildShortestPath(final Hexagon end, final Map<Hexagon, Hexagon> parentMap) {
        List<Hexagon> path = new LinkedList<>();
        Hexagon current = end;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;

    }

    /**
     * Implements a strategic move for the AIs fourth move.
     * @return the selected hexagon for the fourth move.
     */
    @Override
    public Hexagon getFourthMove() {
        List<Hexagon> pathToEastSide = generatePath(this.currentGame.getPlayerLastMove(this),
            new LinkedList<>());
        Hexagon hexagonToPlace  = getWestNorthHexagon(pathToEastSide);
        if (hexagonToPlace != null) {
            return hexagonToPlace;
        }
        return searchRecursiveforPath();

    }

    private List<Hexagon> getAllPlacedHexagons() {
        List<Map<Player, Hexagon>> moves = this.currentGame.getGameMoves();
        List<Hexagon> markedHexagons = new LinkedList<>();
        //Searchs for the hexagons placed by HeroAI in descending order.
        for (int i = moves.size() - 1; i >= 0; i--) {
            Map<Player, Hexagon> current = moves.get(i);
            Hexagon hexagon = current.get(current.keySet().iterator().next());
            if (current.keySet().iterator().next() != this) {
                //If the hexagon was not placed by HeroAI it wont be added.
                continue;
            }
            markedHexagons.add(hexagon);
        }
        return markedHexagons;

    }

    private Hexagon searchRecursiveforPath() {
        List<Hexagon> markedHexagons = getAllPlacedHexagons();
        Hexagon hexagonToPlace = null;
        for (Hexagon hexagon: markedHexagons) {
            currentGame.getGameBoard().setToken(hexagon, GameTokens.EMPTY);
            //Sets the last HeroAI placed Hexagon to empty and searchs a path to the east side.
            Hexagon toReturn = selectHexagonFromPath(hexagon, markedHexagons);
            if (toReturn != null && toReturn != hexagon && !isHexagonAlreadyPlaced(markedHexagons, toReturn)) {
                currentGame.getGameBoard().setToken(hexagon, currentGame.getPlayerToken(this));
                hexagonToPlace = toReturn;
                //If a path is found the hexagon with the given conditions will be returned.
                break;
            }

        }
        markPlacedHexagons(markedHexagons);
        if (hexagonToPlace == null) {
            //If no path is found the first free hexagon will be returned.
            return getFirstFreeHexagon();
        }
        return hexagonToPlace;

    }


    private void markPlacedHexagons(final List<Hexagon> placedHexagons) {
        /*All the hexagons that were placed by HeroAI but in the recursive search for a path wew marked with the
        empty game token , will be marked back to the HeroAI game token.
         */
        for (Hexagon hexagon : placedHexagons) {
            currentGame.getGameBoard().setToken(hexagon, currentGame.getPlayerToken(this));
        }

    }


    private Hexagon selectHexagonFromPath(final Hexagon hexagon, final List<Hexagon> unplacedHexagons) {
        List<Hexagon> hexagonsToPlace = generatePath(hexagon, unplacedHexagons);
        if (unplacedHexagons != null) {
            return getHexagonToPlace(hexagonsToPlace, unplacedHexagons);
        }
        return null;

    }


    private Hexagon getWestNorthHexagon(final List<Hexagon> hexagons) {
        if (hexagons == null || hexagons.isEmpty()) {
            return null;
        }
        Hexagon toReturnHexagon = new Hexagon(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Hexagon hexagon : hexagons) {
            /*Searchs for the hexagons matching the given coordinates conditions. The hexagon to return can
            not already be placed.
             */
            if (isHexagonNorther(toReturnHexagon, hexagon)) {
                toReturnHexagon = hexagon;
            }
        }
        if (toReturnHexagon.getyCoordinate() == Integer.MAX_VALUE) {
            return null;
        }
        return toReturnHexagon;

    }

    private boolean isHexagonAlreadyPlaced(final List<Hexagon> placedHexagon, final Hexagon hexagonToCheck) {
        if (placedHexagon == null) {
            return false;
        }
        for (Hexagon hexagon : placedHexagon) {
            //Compares the hexagons by their cordinates since the content might not be the same.
            if (hexagonToCheck.getxCoordinate() == hexagon.getxCoordinate()
                && hexagonToCheck.getyCoordinate() == hexagon.getyCoordinate()) {
                return true;
            }
        }
        return false;

    }

    private List<Hexagon> generatePath(final Hexagon hexagonToCheck, final List<Hexagon> placedNodes) {
        if (hexagonToCheck.getxCoordinate() == currentGame.getGameBoard().getBoardSize() - 1) {
            return new LinkedList<>();
        }
        return connectToEastSide(hexagonToCheck, placedNodes);

    }


    private List<Hexagon> connectToEastSide(final Hexagon hexagonToCheck, final List<Hexagon> placedNodes) {
        int pathSize = 0;
        List<Hexagon> hexagonsToPlace = new LinkedList<>();
        boolean isFirstPath = true;
        for (Hexagon hexagonNeighbour : getEmptyNeighbours(hexagonToCheck)) {
            for (Hexagon eastHexagon : getEastSideHexagons()) {
                boolean containsPath = false;
                List<Hexagon> posiblePath = getPathToEastSide(hexagonNeighbour, eastHexagon);
                if (isFirstPath && !posiblePath.isEmpty()) {
                    pathSize = posiblePath.size();
                    isFirstPath = false;
                    hexagonsToPlace.add(hexagonNeighbour);
                } else if (pathSize > posiblePath.size()
                    && !posiblePath.isEmpty()) {
                    hexagonsToPlace.clear();
                    containsPath = true;
                    pathSize = posiblePath.size();
                } else if (pathSize == posiblePath.size()
                    && !posiblePath.isEmpty()) {
                    containsPath = true;
                }
                if (containsPath) {
                    if (isHexagonAlreadyPlaced(placedNodes, hexagonNeighbour)) {
                        hexagonsToPlace.addAll(posiblePath);
                    } else {
                        hexagonsToPlace.add(hexagonNeighbour);
                    }
                }
            }

        }
        return hexagonsToPlace;

    }

    private List<Hexagon> getEastSideHexagons() {
        List<Hexagon> eastSideHexagons = new LinkedList<>();
        for (int i = 0; i < this.currentGame.getGameBoard().getBoardSize(); i++) {
            Hexagon wallHexagon = this.currentGame.getGameBoard().getHexagon(
                this.currentGame.getGameBoard().getBoardSize() - 1, i);
            if (wallHexagon.getContent() == GameTokens.EMPTY) {
                eastSideHexagons.add(wallHexagon);
            }
        }
        return eastSideHexagons;

    }

    private List<Hexagon> getEmptyNeighbours(final Hexagon hexagon) {
        List<Hexagon> emptyHexagons = new LinkedList<>();
        for (Hexagon neighbour : this.currentGame.getGameBoard().getHexagonNeighbours(hexagon)) {
            if (neighbour.getContent() == GameTokens.EMPTY) {
                emptyHexagons.add(neighbour);
            }
        }
        return emptyHexagons;

    }

    /**
     * Determines the northmost hexagonOne among two board hexagonOne based on their coordinates.
     * @param hexagonOne the first board hexagonOne to compare.
     * @param hexagonTwo the second board hexagonOne to compare.
     * @return true if hexagonOne is the northernmost hexagon, false otherwise.
     */
    private boolean isHexagonNorther(final Hexagon hexagonOne, final Hexagon hexagonTwo) {
        return hexagonTwo.getxCoordinate() < hexagonOne.getxCoordinate()
            && hexagonOne.getxCoordinate() != hexagonTwo.getxCoordinate()
            || hexagonOne.getxCoordinate() == hexagonTwo.getxCoordinate()
                && hexagonTwo.getyCoordinate() < hexagonOne.getyCoordinate();
    }

    private Hexagon getHexagonToPlace(final List<Hexagon> path, final List<Hexagon> placedHexagons) {
        List<Hexagon> toReturnPath = new LinkedList<>();
        for (Hexagon hexagon : path) {
            if (!isHexagonAlreadyPlaced(placedHexagons, hexagon)) {
                toReturnPath.add(hexagon);
            }
        }
        Hexagon toReturnHexagon = new Hexagon(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Hexagon hexagon : toReturnPath) {
            if (hexagon.getxCoordinate() < toReturnHexagon.getxCoordinate()
                || hexagon.getxCoordinate()  == toReturnHexagon.getxCoordinate()
                && hexagon.getyCoordinate() < toReturnHexagon.getyCoordinate()) {
                toReturnHexagon = hexagon;
            }
        }
        if (toReturnHexagon.getyCoordinate() == Integer.MAX_VALUE) {
            return null;
        }
        return toReturnHexagon;
    }

}
