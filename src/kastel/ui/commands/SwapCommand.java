package kastel.ui.commands;

import kastel.model.Game;
import kastel.model.HexagonPrime;
import kastel.ui.CommandHandler;
import kastel.ui.HexagonPrimeCommand;
import kastel.ui.Result;
import kastel.ui.ResultType;

/**
 * This command swaps the game Tokens for the players in the HexagonPrime game.
 * @author ucxug
 * @version 1.0
 */

public class SwapCommand extends HexagonPrimeCommand {

    /**
     * Represents the command name.
     */
    private static final String COMMAND_NAME = "swap";
    /**
     * Represent the error for when the swap movements is not posible since the game is not in the second turn.
     */
    private static final String INVALID_SWAP_TURN = "the swap function is only valid in the second game turn.";
    /**
     * Represents the expected number of arguments.
     */
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private static final int EXPECTED_MOVEMENTS_SIZE = 1;

    /**
     * Intantiates a Swap command.
     * @param commandHandler the commandHandler
     * @param hexagonPrime the hexagonPrime instance.
     */
    public SwapCommand(final CommandHandler commandHandler, final HexagonPrime hexagonPrime) {
        super(COMMAND_NAME, commandHandler, hexagonPrime, EXPECTED_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the command with the given arguments.
     * @param commandArguments the arguments of the command
     */
    @Override
    protected Result executeTaskCommand(final String[] commandArguments) {
        Game currentGame = hexagonPrime.getCurrentGame();
        if (currentGame.getGameMoves().size() != EXPECTED_MOVEMENTS_SIZE
            ||  currentGame.getTurnsCount() != EXPECTED_MOVEMENTS_SIZE) {
            return new Result(ResultType.FAILURE, INVALID_SWAP_TURN, false, false);
        }
        hexagonPrime.getCurrentGame().swapMovement();
        hexagonPrime.changeCurrentPlayer();
        return new Result(ResultType.SUCCESS, SWAP_MOVEMENT_FORMAT.formatted(
            hexagonPrime.getCurrentGame().getCurrentPlayer().getEnemyPlayer().getName()),
            true, true);
    }
}
