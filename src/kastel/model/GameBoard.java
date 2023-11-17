package kastel.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A class representing the game board that consist of hexagons.
 * This class represents the board for a Hexagon Prime game.
 * @author ucxug
 * @version 1.0
 */

public class GameBoard {


    private static final String BOARD_INDENT = " ";
    private static final int DIVISION_BY_MODULO = 2;
    private static final int EVEN_COORDINATE = 0;
    private static final int X_COORDINATE_INDEX = 0;
    private static final int Y_COORDINATE_INDEX = 1;
    private static final String LINE_DELIMITER = System.lineSeparator();

    /**
     * Represents the board in the game.
     */
    private final Hexagon[][] board;

    /**
     * Represents the size/legth of the baord.
     */
    private final int boardSize;

    /**
     * Constructs a game board of the specified size.
     * @param boardSize the size of the game board.
     */
    public GameBoard(final int boardSize) {
        this.board = new Hexagon[boardSize][boardSize];
        this.boardSize = boardSize;
        buildBoard();
    }


    private void buildBoard() {
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                Hexagon hexagon = new Hexagon(j, i);
                this.board[j][i] = hexagon;
            }
        }
    }

    /**
     * Gets the game board.
     * @return the graph representing the game board.
     */
    public Hexagon[][] getBoard() {
        return Arrays.copyOf(this.board, this.board.length);
    }



    /**
     * Generates a String representation of the game board.
     * @return the String representation of the game board.
     */
    public String toString() {
        StringJoiner boardRepresentation = new StringJoiner(LINE_DELIMITER);
        for (int i = 0; i < this.boardSize; i++) {
            String indent = BOARD_INDENT.repeat(i);
            StringJoiner boardRow = new StringJoiner(BOARD_INDENT);
            for (int j = 0; j < this.boardSize; j++) {
                boardRow.add(getHexagon(j, i).getContent().getTokenRepresentation());
            }
            boardRepresentation.add(indent + boardRow);
        }
        return boardRepresentation.toString();
    }

    /**
     * Gets a specified hexagon on the game board.
     * @param xCoordinate the x coordinate of the hexagon.
     * @param yCoordinate the y coordinate of the hexagon.
     * @return the hexagon at the corresponding coordinates.
     */
    public Hexagon getHexagon(final int xCoordinate, final int yCoordinate) {
        return this.board[xCoordinate][yCoordinate];
    }

    /**
     * Sets the token on a specified hexagon of the game board.
     * @param hexagon the hexagon to set the token on.
     * @param gameToken the token to set on the hexagon.
     */
    public void setToken(final Hexagon hexagon, final GameTokens gameToken) {
        //The hexagon will be seted in the board with the given token.
        getHexagon(hexagon.getxCoordinate(), hexagon.getyCoordinate()).setContent(gameToken);
    }


    /**
     * Checks whether the given coordinates are valid on the game board.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValidCoordinate(final int xCoordinate, final int yCoordinate) {
        //The x and y coordinate must be less than the board size and bigger/equal to 0.
        return xCoordinate >= 0 && xCoordinate < this.boardSize && yCoordinate >= 0 && yCoordinate < this.boardSize;
    }



    /**
     * Marks the winning path for the specified token.
     * @param currentToken The token for which the winning path needs to be marked.
     */
    public void markWinningPath(final GameTokens currentToken) {
        for (Hexagon[] hexagons : this.board) {
            for (Hexagon winningHexagon : hexagons) {
                if (winningHexagon.getContent() == GameTokens.WIN_TOKEN) {
                    //If posibl the winning hexagon neigbours will be also marked.
                    for (Hexagon neighbor : getHexagonNeighbours(winningHexagon)) {
                        if (neighbor.getContent() == currentToken) {
                            neighbor.setContent(GameTokens.WIN_TOKEN);
                            markWinningPath(currentToken);
                        }
                    }
                }
            }
        }
    }


    /**
     * Marks the hexagons forming the winning path.
     * @param path the path to be marked.
     */
    public void markWinningHexagons(final List<Hexagon> path) {
        for (Hexagon hexagon : path) {
            getHexagon(hexagon.getxCoordinate(), hexagon.getyCoordinate()).setContent(GameTokens.WIN_TOKEN);
        }
    }


    /**
     * Gets the size of the game board.
     * @return The size of the game board.
     */
    public int getBoardSize() {
        return boardSize;
    }


    /**
     * Gets the Hexagon neighbors for the given hexagon.
     * @param hexagon the hexagon to be checked.
     * @return the hexagon neighbours.
     */
    public List<Hexagon> getHexagonNeighbours(final Hexagon hexagon) {
        if (hexagon.getyCoordinate() % DIVISION_BY_MODULO == EVEN_COORDINATE) {
            return connectHexagonsEvenRow(hexagon.getxCoordinate(), hexagon.getyCoordinate());
        }
        return new LinkedList<>(connectHexagonOddRow(hexagon.getxCoordinate(), hexagon.getyCoordinate()));
    }

    private List<Hexagon> connectHexagons(final int xCoordinate, final int yCoordinate,
                                          final int[][] neighborsCoordinates) {
        List<Hexagon> hexagons = new LinkedList<>();
        for (int[] coordinates : neighborsCoordinates) {
            int neighborX = xCoordinate + coordinates[X_COORDINATE_INDEX];
            int neighborY = yCoordinate + coordinates[Y_COORDINATE_INDEX];
            if (isValidCoordinate(neighborX, neighborY)) {
                Hexagon neighbor = getHexagon(neighborX, neighborY);
                //Searchs for the given hexagon neighbours.
                if (neighbor != null) {
                    hexagons.add(neighbor);
                }
            }
        }
        return hexagons;
    }

    private List<Hexagon> connectHexagonsEvenRow(final int xCoordinate, final int yCoordinate) {
        int[][] evenRowCoordinates = {{0, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}, {-1, 1}};
        return connectHexagons(xCoordinate, yCoordinate, evenRowCoordinates);
    }

    private List<Hexagon> connectHexagonOddRow(final int xCoordinate, final int yCoordinate) {
        int[][] oddRowCoordinates = {{0, -1}, {1, 0}, {0, 1}, {-1, 1}, {1, -1}, {-1, 0}};
        return connectHexagons(xCoordinate, yCoordinate, oddRowCoordinates);
    }

}
