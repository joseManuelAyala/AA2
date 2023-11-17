package kastel.ui.commands;

import java.util.StringJoiner;
import kastel.model.Game;
import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command list all the games that are not over.
 * @author ucxug
 * @version 1.0
 */
public class ListGamesCommand extends HexagonPrimeCommand {
    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "list-games";
    /**
     * Represents the delimiter between each game that is printed.
     */
    private static final String LINE_DELIMITER = System.lineSeparator();
    /**
     * Represents the expected number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    /**
     * Represents the format for the game to be printed.
     */
    private static final String LIST_GAMES_FORMAT = "%s: %d";

    /**
     * Intantiates a ListGames command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public ListGamesCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
        super(COMMAND_NAME, commandHandler, hexagonPrime, EXPECTED_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        StringJoiner gamesInformation = new StringJoiner(LINE_DELIMITER);
        for (Game game : hexagonPrime.getGames()) {
            if (!game.isGameOver(true)) {
                gamesInformation.add(LIST_GAMES_FORMAT.formatted(game.getGameName(), game.getGameMoves().size()));
            }
        }
        return new Result(ResultType.SUCCESS, gamesInformation.toString(), false, false);
    }

}
