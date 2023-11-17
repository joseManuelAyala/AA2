package kastel.ui.commands;

import kastel.model.Game;
import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command adds a new HexagonPrime game.
 * @author ucxug
 * @version 1.0
 */

public class NewGameCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "new-game";
    /**
     * Represents the error for when the given game name is not valid.
     */
    private static final String EMPTY_NAME_ERROR = "the given name is not valid.";
    /**
     * Represent the error for when a game has already been added.
     */
    private static final String GAME_NAME_EXIST_ERROR = "the game name already exist and can not be added";
    /**
     * Represents the format for when a new game has been added.
     */
    private static final String NEW_GAME_WELCOME_MESSAGE = "Welcome to %s";
    /**
     * Represents the expected number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    /**
     * Represents the index of the new game to be added.
     */
    private static final int GAME_NAME_INDEX = 0;

    /**
     * Intantiates a NewGame command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public NewGameCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
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
        String gameName = commandArguments[GAME_NAME_INDEX];
        if (gameName.isEmpty()) {
            return new Result(ResultType.FAILURE, EMPTY_NAME_ERROR, false, false);
        }
        Game newGame = hexagonPrime.getGame(gameName);
        //There can not be two games with the same name.
        if (newGame != null) {
            return new Result(ResultType.FAILURE, GAME_NAME_EXIST_ERROR, false, false);
        }
        hexagonPrime.addNewGame(gameName);
        //Adds a new game and sets it as the current game.
        return new Result(ResultType.SUCCESS, NEW_GAME_WELCOME_MESSAGE.formatted(gameName),
            true, true);

    }
}
