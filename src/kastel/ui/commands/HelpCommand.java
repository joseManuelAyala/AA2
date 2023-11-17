package kastel.ui.commands;

import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;


/**
 * This command prints all the command functions.
 * @author ucxug
 * @version 1.0
 */

public class HelpCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "help";
    /**
     * Represents the expected number of arguments for this command.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    /**
     * Intantiates a Help command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public HelpCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
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
        return new Result(ResultType.SUCCESS, getCommandFunctions(), false, false);
    }
}
