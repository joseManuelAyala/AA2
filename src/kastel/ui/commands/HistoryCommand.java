package kastel.ui.commands;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import kastel.model.HexagonPrime;
import kastel.model.Hexagon;
import kastel.model.Player;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command shows the given number of game moves in the HexagonPrime game.
 * @author ucxug
 * @version 1.0
 */

public class HistoryCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "history";
    /**
     * Represents the index of posibles movements to be retrieved.
     */
    private static final int MOVEMENTS_NUMBER_INDEX = 0;
    private static final int EMPTY_MOVEMENTS_SIZE = 0;
    /**
     * Represents the format for the players movements.
     */
    private static final String PLAYER_MOVEMENT_FORMAT = "%s: %d %d";
    /**
     * Represents the error for when no movements have been made.
     */
    private static final String NO_MOVEMENTS_ERROR = "the current game has no movements yet.";
    /**
     * Represents the error for when the given number of movements to be retrieved is not valid.
     */
    private static final String INVALID_NUMBER_MOVEMENTS_ERROR = "the given number of movements to show is not valid.";
    /**
     * Represents the expected number of movements.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    /**
     * Intantiates a History command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public HistoryCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
        super(COMMAND_NAME, commandHandler, hexagonPrime, EXPECTED_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        int numberOfMovements;
        if (commandArguments.length != EXPECTED_NUMBER_OF_ARGUMENTS) {
            return gameLastMove();
        } else {
            try {
                numberOfMovements = Integer.parseInt(commandArguments[MOVEMENTS_NUMBER_INDEX]);
            } catch (NumberFormatException e) {
                return new Result(ResultType.FAILURE, INVALID_NUMBER_MOVEMENTS_ERROR, false,
                    false);
            }
            if (numberOfMovements <= EMPTY_MOVEMENTS_SIZE
                || hexagonPrime.getCurrentGame().getGameMoves().size() - numberOfMovements < EMPTY_MOVEMENTS_SIZE) {
                return new Result(ResultType.FAILURE, INVALID_NUMBER_MOVEMENTS_ERROR, false,
                    false);
            }
            List<Map<Player, Hexagon>> gameMoves = hexagonPrime.getCurrentGame().getGameMoves();
            if (gameMoves.isEmpty()) {
                return new Result(ResultType.FAILURE, NO_MOVEMENTS_ERROR, false, false);
            }
            return getCertainMovements(numberOfMovements);
        }
    }


    private Result getCertainMovements(final int numberOfMovements) {
        List<Map<Player, Hexagon>> reversedMoves = hexagonPrime.getCurrentGame().getGameMoves();
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        int count = 0;
        for (int i = reversedMoves.size() - 1; i >= 0 && count < numberOfMovements; i--) {
            Map<Player, Hexagon> currentMove = reversedMoves.get(i);
            Player player = currentMove.keySet().iterator().next();
            Hexagon movement = currentMove.get(player);
            stringJoiner.add(PLAYER_MOVEMENT_FORMAT.formatted(player.getName(),
                movement.getxCoordinate(), movement.getyCoordinate()));
            count++;
        }
        return new Result(ResultType.SUCCESS, stringJoiner.toString(), false, false);
    }

    
    private Result gameLastMove() {
        if (hexagonPrime.getCurrentGame().getLastMove() == null) {
            return new Result(ResultType.FAILURE, NO_MOVEMENTS_ERROR, false, false);
        }
        Map<Player, Hexagon> currentMove = hexagonPrime.getCurrentGame().getLastMove();
        Player player = currentMove.keySet().iterator().next();
        Hexagon movement = currentMove.get(player);
        String moveFormat = PLAYER_MOVEMENT_FORMAT.formatted(player.getName(),
            movement.getxCoordinate(), movement.getyCoordinate());
        return new Result(ResultType.SUCCESS, moveFormat, false, false);
    }

}
