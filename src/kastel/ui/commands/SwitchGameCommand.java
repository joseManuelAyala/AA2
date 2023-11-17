package kastel.ui.commands;

import kastel.model.Game;
import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command switches to another already added HexagonPrime game.
 * @author ucxug
 * @version 1.0
 */

public class SwitchGameCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "switch-game";
    /**
     * Represents the error for when the game to be swichted does not exist.
     */
    private static final String GAME_DOESNT_EXIST_ERROR = "the game %s does not exist.";
    /**
     * Represents the error for when the game to be switched is over and can not be played anymore.
     */
    private static final String GAME_OVER_ERROR = "the game %s is over and can not be switched.";
    /**
     * Represents the confirmation for when the game has been switched.
     */
    private static final String SWITCHED_CONFIRMATION = "Switched to %s";
    /**
     * Represents the error for when the switched game is the current game.
     */
    private static final String SWITCHED_CURRENT_GAME_ERROR = "the game %s is the current game and can not be swichted";
    /**
     * Represents the index of the game name to be switched.
     */
    private static final int GAME_NAME_INDEX = 0;
    /**
     * Represents the expect number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    /**
     * Intantiates a Switch game command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public SwitchGameCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
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
        Game switchedGame = hexagonPrime.getGame(commandArguments[GAME_NAME_INDEX]);
        if (switchedGame == null) {
            //The game must already be added onto Hexagon Prime.
            return new Result(ResultType.FAILURE, GAME_DOESNT_EXIST_ERROR.formatted(commandArguments[GAME_NAME_INDEX]),
                false, false);
        } else if (switchedGame.isGameOver(true)) {
            return new Result(ResultType.FAILURE, GAME_OVER_ERROR.formatted(switchedGame.getGameName()),
                false, false);
        } else if (switchedGame == hexagonPrime.getCurrentGame()) {
            return new Result(ResultType.FAILURE, SWITCHED_CURRENT_GAME_ERROR.formatted(switchedGame.getGameName()),
                false, false);

        }
        hexagonPrime.changeGame(switchedGame);
        //Sets the swichted game as the current game.
        return new Result(ResultType.SUCCESS, SWITCHED_CONFIRMATION.formatted(switchedGame.getGameName()),
            false, false);
    }
}
