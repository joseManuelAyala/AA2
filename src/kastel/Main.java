package kastel;

import kastel.model.AIPlayers;
import kastel.model.ArtificialManager;
import kastel.model.BogoAI;
import kastel.model.HeroAI;
import kastel.model.HexagonPrime;
import kastel.model.Player;
import kastel.ui.CommandHandler;

/**
 * Main entry point for starting the HexagonPrime system.
 * Starts the interaction with the user.
 * @author ucxug
 * @version 1.0
 */
public final class Main {
    private static final String CLASS_NOT_INSTANTIATABLE = "Utility class cannot be instantiated.";
    private static final String INVALID_ARGUMENTS_LENGTH =
        "Error: The given arguments does not have the expected lenght";
    private static final String SAME_NAMES_ERROR = "Error: The players name can not be equal.";
    private static final String PRINT_MESSAGE = "auto-print";
    private static final String INVALID_BOARD_SIZE_EXCEPTION = "Error: The given board size is not valid.";
    private static final String HEXAGON_WELCOME_MESSAGE = "Welcome to Prime";
    private static final String FIRST_MOVE_MESSAGE = "%s's turn";
    private static final int PLAYER_ONE_NAME_INDEX = 1;
    private static final int PLAYER_TWO_NAME_INDEX = 2;
    private static final int MIN_ARGUMENTS_LENGTH = 3;
    private static final int MIN_BOARD_SIZE = 4;
    private static final int ODD_NUMBER = 1;
    private static final int MAX_ARGUMENTS_LENGTH = 4;
    private static final int BOARD_SIZE_INDEX = 0;
    private static final int MAXIMAL_BOARD_SIZE = 12345;
    private static final int PRINT_BOARD_VALUE_INDEX = 3;
    private static final int INVALID_SIZE = -1;
    private static final int EVEN_DIVISION = 2;

    private Main() {
        throw new UnsupportedOperationException(CLASS_NOT_INSTANTIATABLE);
    }

    /**
     * The main method is the entry point for the programm, it serves to handle all the user interaction with,
     * the programm.
     * @param args the command arguments, the players name, the board size and the indicator for printing the
     *             game board after each turn.
     */
    public static void main(final String[] args) {
        if (args.length < MIN_ARGUMENTS_LENGTH || args.length > MAX_ARGUMENTS_LENGTH) {
            System.err.println(INVALID_ARGUMENTS_LENGTH);
            return;
        }
        String playerOneName = args[PLAYER_ONE_NAME_INDEX];
        String playerTwoName = args[PLAYER_TWO_NAME_INDEX];
        if (playerOneName.equals(playerTwoName) || equalsArtificialName(playerOneName)) {
            System.err.println(SAME_NAMES_ERROR);
            return;
        }
        Player playerOne = new Player(playerOneName);
        boolean isAIPlayer = true;
        Player playerTwo = getArtificialPlayer(playerTwoName);
        if (playerTwo == null) {
            playerTwo = new Player(playerTwoName);
            isAIPlayer = false;
        }
        int boardSize = getBoardSize(args[BOARD_SIZE_INDEX]);
        if (boardSize == INVALID_SIZE) {
            System.err.println(INVALID_BOARD_SIZE_EXCEPTION);
            return;
        }
        boolean mustBePrinted =
            args.length == MAX_ARGUMENTS_LENGTH && args[PRINT_BOARD_VALUE_INDEX].equals(PRINT_MESSAGE);

        HexagonPrime hexagonPrime = new HexagonPrime(boardSize, playerOne, playerTwo);
        CommandHandler commandHandler = new CommandHandler(hexagonPrime, mustBePrinted, isAIPlayer);
        if (isAIPlayer) {
            ArtificialManager artificialManager = new ArtificialManager(playerTwo);
            commandHandler.setArtificialManager(artificialManager);
        }
        System.out.println(HEXAGON_WELCOME_MESSAGE);
        if (mustBePrinted) {
            System.out.println(hexagonPrime.getCurrentGame().getGameBoard().toString());
        }
        System.out.println(FIRST_MOVE_MESSAGE.formatted(playerOne.getName()));
        commandHandler.handleUserInput();
    }



    private static int getBoardSize(String boardSize) {
        int size;
        try {
            size = Integer.parseInt(boardSize);
        } catch (NumberFormatException e) {
            return INVALID_SIZE;
        }
        if (size < MIN_BOARD_SIZE || size % EVEN_DIVISION != ODD_NUMBER || size > MAXIMAL_BOARD_SIZE) {
            return INVALID_SIZE;

        }
        return size;

    }

    private static boolean equalsArtificialName(String name) {
        for (AIPlayers player : AIPlayers.values()) {
            if (player.getPlayerName().equals(name)) {
                return true;
            }
        }
        return false;

    }

    private static Player getArtificialPlayer(String name) {
        if (name.equals(AIPlayers.BogoAI.getPlayerName())) {
            return new BogoAI();
        } else if (name.equals(AIPlayers.HeroAI.getPlayerName())) {
            return new HeroAI();
        }
        return null;

    }


}
