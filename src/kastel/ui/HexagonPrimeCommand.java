package kastel.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import kastel.model.HexagonPrime;
import kastel.model.Hexagon;
import kastel.model.MoveResult;

/**
 * This class represents a command for the HexagonPrime game.
 * This class was taken from the solution for Task number 5 of SS 2023 but has been modified and
 * contains new implementations and methods.
 * @author Programmieren-Team
 * @version 1.0
 */

public abstract class HexagonPrimeCommand extends Command {

    /**
     * Represents the format for when the player swaps the tokens.
     */
    protected static final String SWAP_MOVEMENT_FORMAT = "%s swaps";
    /**
     * Represents the format for when a given player has won the game.
     */
    protected static final String WINNING_FORMAT_MESSAGE = "%s wins!";

    /**
     * Represents the error for when more arguments than expected are given to a commmand.
     */
    protected static final String MORE_ARGUMENTS_THAN_EXPECTED = "the command %s needs only %d arguments.";
    /**
     * Represents the delimiter for strings.
     */
    protected static final String LINE_DELIMITER = System.lineSeparator();
    /**
     * Represents the function of the switch-game command.
     */
    private static final String SWITCH_GAMES_COMMAND_FUNCTION =
        "* switch-game: allows you to sitch the curernt game"
            + " to a different one.";

    private static final String HELP_COMMAND_FUNCTION = "* help: Prints this help message";

    /**
     * Represents the function of the swap command.
     */
    private static final String SWAP_COMMAND_FUNCTION = "* swap: changes the colors of the players.";
    /**
     * Represents the function of the quit command.
     */
    private static final String QUIT_COMMAND_FUNCTION = "* quit: Quit all games and end programm.";
    /**
     * Represents the function of the print command.
     */
    private static final String PRINT_COMMAND_FUNCTION = "* print: displays the current game board.";
    /**
     * Represents the function of the place command.
     */
    private static final String PLACE_COMMAND_FUNCTION = "* place: puts a game piece of the game board.";
    /**
     * Represents the function of the new-game command.
     */
    private static final String NEW_GAME_COMMAND_FUNCTION = "* new-game: initiates a new game.";
    /**
     * Represent the function of the list-games command.
     */
    private static final String LIST_GAMES_COMMAND_FUNCTION = "* list-games: displays a list of all currenntly"
        + " ongoing games.";
    /**
     * Represents the function of the history command.
     */
    private static final String HISTORY_COMMAND_FUNCTION = "* history: displays the corrdinates of the hexagon where"
        + " the given moves back, piece was placed.";


    /**
     * Represents the error for when less arguments than expected are given to a command.
     */
    private static final String NOT_EXPECTED_ARGS_LENGTH_ERROR = "Expected %d arguments but got %d.";
    /**
     * Represents the current turn message.
     */
    private static final String CURRENT_PLAYERS_TURN_FORMAT = "%s's turn";
    /**
     * Represents the format for the AI player when placing a Game token.
     */
    private static final String AI_PLAYER_SET_FORMAT = "%s places at %d %d";
    /**
     * Represents the HexagonPrime instance.
     */
    protected final HexagonPrime hexagonPrime;

    /**
     * Represents all the command functions in a list.
     */
    private final List<String> commandInformation;
    /**
     * Represents the expected number of arguments.
     */
    private final int expectedNumberOfArguments;
    private final String commandFunctions;


    /**
     * Constructs a new hexagonPrime command.
     * @param commandName               the name of the command.
     * @param commandHandler            the command handler.
     * @param hexagonPrime              the task administrator.
     * @param expectedNumberOfArguments the expected leght for the command arguments.
     */
    protected HexagonPrimeCommand(final String commandName, final CommandHandler commandHandler,
                                  final HexagonPrime hexagonPrime, final int expectedNumberOfArguments) {
        super(commandName, commandHandler);
        this.hexagonPrime = Objects.requireNonNull(hexagonPrime);
        this.expectedNumberOfArguments = expectedNumberOfArguments;
        this.commandInformation = new LinkedList<>();
        this.commandFunctions = String.join(LINE_DELIMITER, generateCommandFunctions());
    }

    @Override
    public final void execute(final String[] commandArguments) {
        if (commandArguments.length > expectedNumberOfArguments) {
            ResultType.FAILURE.printResult(NOT_EXPECTED_ARGS_LENGTH_ERROR, expectedNumberOfArguments,
                commandArguments.length);
            return;
        }
        Result result = executeTaskCommand(commandArguments);
        if (result != null) {
            if (result.getMessage() != null) {
                result.getType().printResult(result.getMessage());
            }
            if (commandHandler.isAutoPrint() && result.isCommandPrint()
                && !hexagonPrime.getCurrentGame().isGameOver(true)) {
                System.out.println(hexagonPrime.getCurrentGame().getGameBoard().toString());
            }
        }
        if (result != null && result.isPrintTurnsMessage()
            && !hexagonPrime.getCurrentGame().isGameOver(true)) {
            printPlayersTurns();
        }
        executeArtifialPlayer();
    }

    /**
     * Hadles the innteraction with the Artificial player.
     */
    private void executeArtifialPlayer() {

        if (isAiPlayerTurn()) {
            commandHandler.getArtificialManager().setCurrentGame(hexagonPrime.getCurrentGame());
            MoveResult moveResult = commandHandler.getArtificialManager().getArtificialMove();
            Hexagon toPlaceHexagon = moveResult.getMovement();
            boolean isSwap = moveResult.isSwap();

            if (toPlaceHexagon != null && !isSwap) {
                hexagonPrime.setGameToken(toPlaceHexagon);
                printArtificialMove(toPlaceHexagon);
                if (commandHandler.isAutoPrint()
                    && !hexagonPrime.getCurrentGame().isGameOver(true)) {
                    printCurrentGame();
                }

            } else if (isSwap) {
                hexagonPrime.getCurrentGame().swapMovement();
                hexagonPrime.changeCurrentPlayer();
                printSwapMovement();
                if (commandHandler.isAutoPrint()) {
                    printCurrentGame();
                }
            }
            if (hexagonPrime.getCurrentGame().isGameOver(true)) {
                printWinningBoard();

            } else if (!hexagonPrime.getCurrentGame().isGameOver(true)) {
                printPlayersTurns();
            }
        }
    }

    /**
     * Executes the command.
     *
     * @param commandArguments the command arguments
     * @return the result of the command
     */
    protected abstract Result executeTaskCommand(String[] commandArguments);

    /**
     * Gets all  the function of all of the commands toghether in a list for the HexagonPrime intances.
     * @return a list wiht the function of all commands.
     */
    private List<String> generateCommandFunctions() {
        this.commandInformation.add(HELP_COMMAND_FUNCTION);
        this.commandInformation.add(HISTORY_COMMAND_FUNCTION);
        this.commandInformation.add(LIST_GAMES_COMMAND_FUNCTION);
        this.commandInformation.add(NEW_GAME_COMMAND_FUNCTION);
        this.commandInformation.add(PLACE_COMMAND_FUNCTION);
        this.commandInformation.add(PRINT_COMMAND_FUNCTION);
        this.commandInformation.add(QUIT_COMMAND_FUNCTION);
        this.commandInformation.add(SWAP_COMMAND_FUNCTION);
        this.commandInformation.add(SWITCH_GAMES_COMMAND_FUNCTION);
        return new LinkedList<>(this.commandInformation);

    }



    /**
     * Returns the string represention of all the command functions.
     * @return the string represtation of the functions.
     */
    protected String getCommandFunctions() {
        return this.commandFunctions;
    }


    private boolean isAiPlayerTurn() {
        return commandHandler.isAIPlayer()
            && hexagonPrime.getCurrentPlayer() == commandHandler.getArtificialManager().getArtificialPlayer()
            && !hexagonPrime.getCurrentGame().isGameOver(true);
    }

    private void printPlayersTurns() {
        System.out.println(CURRENT_PLAYERS_TURN_FORMAT.formatted(
            hexagonPrime.getCurrentGame().getCurrentPlayer().getName()));
    }

    private void printCurrentGame() {
        System.out.println(hexagonPrime.getCurrentGame().getGameBoard().toString());
    }

    private void printWinningBoard() {
        System.out.println(WINNING_FORMAT_MESSAGE.formatted(
            hexagonPrime.getCurrentPlayer().getEnemyPlayer().getName()));
        System.out.println(hexagonPrime.getCurrentGame().getWinningBoard().toString());
    }

    private void printSwapMovement() {
        System.out.println(SWAP_MOVEMENT_FORMAT.formatted(
            commandHandler.getArtificialManager().getArtificialPlayer().getName()));
    }

    private void printArtificialMove(final Hexagon toPlaceHexagon) {
        System.out.println(AI_PLAYER_SET_FORMAT.formatted(
            commandHandler.getArtificialManager().getArtificialPlayer().getName(),
            toPlaceHexagon.getxCoordinate(), toPlaceHexagon.getyCoordinate()));
    }
}
