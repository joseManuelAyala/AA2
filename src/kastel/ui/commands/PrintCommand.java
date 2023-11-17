package kastel.ui.commands;

import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command prints the current HexagonPrime game board.
 * @author ucxug
 * @version 1.0
 */

public class PrintCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "print";
    /**
     * Represents the expected number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    /**
     * Intantiates a Print command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public PrintCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
        super(COMMAND_NAME, commandHandler, hexagonPrime, EXPECTED_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        String currentGameBoard = hexagonPrime.getCurrentGame().getGameBoard().toString();
        return new Result(ResultType.SUCCESS, currentGameBoard, false, false);
    }
}
