package kastel.ui.commands;

import kastel.model.GameTokens;
import kastel.model.HexagonPrime;
import kastel.model.Hexagon;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command place a GameToken for the current player in the HexagonPrime game board.
 * @author ucxug
 * @version 1.0
 */

public class PlaceCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "place";
    /**
     * Represents the index of the x coordinate.
     */
    private static final int X_COORDINATE_INDEX = 0;
    /**
     * Represents the index of the y coordinate.
     */
    private static final int Y_COORDINATE_INDEX = 1;
    /**
     * Represents the error for when the current game is over and can not be played anymore.
     */
    private static final String GAME_OVER_ERROR = "the current game is over and can not be played anymore.";
    /**
     * Represents the error for a invalid x coordinate.
     */
    private static final String INVALID_COORDINATE_ERROR = "the given coordinates are not valid";

    /**
     * Represents the error for when the coordinate position is already occuppied.
     */
    private static final String INVALID_BOARD_POSITION = "Tile already placed";
    /**
     * Represents the expected number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;
    private static final int INVALID_COORDINATE = -1;
    private static final int ZERO_COORDINATE = 0;

    /**
     * Intantiates a Place command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public PlaceCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
        super(COMMAND_NAME, commandHandler, hexagonPrime, EXPECTED_NUMBER_OF_ARGUMENTS);
    }


    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        if (commandArguments.length != EXPECTED_NUMBER_OF_ARGUMENTS) {
            return new Result(ResultType.FAILURE, MORE_ARGUMENTS_THAN_EXPECTED.formatted(COMMAND_NAME,
                EXPECTED_NUMBER_OF_ARGUMENTS), false, false);
        }
        if (hexagonPrime.getCurrentGame().isGameOver(true)) {
            //No token can be place if the current game is over.
            return new Result(ResultType.FAILURE, GAME_OVER_ERROR, false, false);
        }
        int xCoordinate = getCoordinate(commandArguments[X_COORDINATE_INDEX]);
        int yCoordinate = getCoordinate(commandArguments[Y_COORDINATE_INDEX]);
        if (xCoordinate == INVALID_COORDINATE || yCoordinate == INVALID_COORDINATE) {
            return new Result(ResultType.FAILURE, INVALID_COORDINATE_ERROR, false, false);
        }
        Hexagon hexagon = hexagonPrime.getCurrentGame().getGameBoard().getHexagon(xCoordinate, yCoordinate);
        //The hexagon must be free (with the empty token ) to so that the game token can be placed on it.
        if (hexagon == null || !hexagon.getContent().getTokenRepresentation().equals(
            GameTokens.EMPTY.getTokenRepresentation())) {
            return new Result(ResultType.FAILURE, INVALID_BOARD_POSITION, false, false);
        }

        return placeHexagon(hexagon);

    }

    private Result placeHexagon(final Hexagon hexagonToPlace) {

        hexagonPrime.setGameToken(hexagonToPlace);
        if (hexagonPrime.getCurrentGame().isGameOver(true)) {
            return new Result(ResultType.SUCCESS,
                WINNING_FORMAT_MESSAGE.formatted(
                    hexagonPrime.getCurrentGame().getCurrentPlayer().getEnemyPlayer().getName())
                    + LINE_DELIMITER
                    + hexagonPrime.getCurrentGame().getWinningBoard().toString(), true, false);
        }
        return new Result(ResultType.SUCCESS, true, true);

    }


    private int getCoordinate(final String coordinateRepresentation) {
        int coordinate;
        try {
            coordinate = Integer.parseInt(coordinateRepresentation);
        } catch (NumberFormatException e) {
            return INVALID_COORDINATE;
        }
        if (coordinate >= hexagonPrime.getCurrentGame().getGameBoard().getBoardSize()
            ||  coordinate < ZERO_COORDINATE) {
            return INVALID_COORDINATE;
        }
        return coordinate;

    }

}
