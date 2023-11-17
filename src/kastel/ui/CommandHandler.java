/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package kastel.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import kastel.model.ArtificialManager;
import kastel.model.HexagonPrime;
import kastel.ui.commands.HelpCommand;
import kastel.ui.commands.HistoryCommand;
import kastel.ui.commands.ListGamesCommand;
import kastel.ui.commands.NewGameCommand;
import kastel.ui.commands.PlaceCommand;
import kastel.ui.commands.PrintCommand;
import kastel.ui.commands.QuitCommand;
import kastel.ui.commands.SwapCommand;
import kastel.ui.commands.SwitchGameCommand;

/**
 * This class handles the user input and executes the commands.
 * This class was taken from the solution for ÜB 5 from SS 23.
 * @author Programmieren-Team
 */
public final class CommandHandler {

    /**
     * Represents String to split input into individual components.
     */
    private static final String COMMAND_SEPARATOR_REGEX = "\\s+";
    /**
     * Represents the error for when a command is not found.
     */
    private static final String COMMAND_NOT_FOUND = "Error: Command '%s' not found%n";
    /**
     * Represents the Hexagon Prime instance responsible for managing game instances and players.
     */
    private final HexagonPrime hexagonPrime;
    /**
     * A map of commands and their names.
     */
    private final Map<String, Command> commands;
    /**
     * Indicates wheter the game board needs to be printed after each turn.
     */
    private final boolean autoPrint;
    /**
     * Manages AI players and desicions in the game.
     */
    private ArtificialManager artificialManager;
    /**
     * Indicates whether an AI player is involved in the game.
     */
    private final boolean aIPlayer;
    /**
     * Indicates whether the command handler is running.
     */
    private boolean running = false;


    /**
     * Intantiates a new command handler.
     * @param hexagonPrime the santorini game.
     * @param autoPrint Indicates wether the board game needs to be printed after each turn.
     * @param isAIPlayer Indicates whether the second player is a AI Player.
     *
     */
    public CommandHandler(final HexagonPrime hexagonPrime, boolean autoPrint, boolean isAIPlayer) {
        this.aIPlayer = isAIPlayer;
        this.autoPrint = autoPrint;
        this.hexagonPrime = Objects.requireNonNull(hexagonPrime);
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Handles the user input.
     */
    public void handleUserInput() {
        this.running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNext()) {
                executeCommand(scanner.nextLine());
            }
        }
    }


    /**
     * Quits the user input handling.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(final String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        if (!commands.containsKey(commandName)) {
            System.err.print(COMMAND_NOT_FOUND.formatted(commandName));
            return;
        }

        commands.get(commandName).execute(commandArguments);
    }

    /**
     * Sets the Artificial Manager instace.
     * @param artificialManager the Aritificial Manager instace.
     */
    public void setArtificialManager(final ArtificialManager artificialManager) {
        this.artificialManager = artificialManager;
    }

    private void initCommands() {
        this.addCommand(new HelpCommand(this, hexagonPrime));
        this.addCommand(new HistoryCommand(this, hexagonPrime));
        this.addCommand(new ListGamesCommand(this, hexagonPrime));
        this.addCommand(new NewGameCommand(this, hexagonPrime));
        this.addCommand(new PlaceCommand(this, hexagonPrime));
        this.addCommand(new PrintCommand(this, hexagonPrime));
        this.addCommand(new QuitCommand(this, hexagonPrime));
        this.addCommand(new SwapCommand(this, hexagonPrime));
        this.addCommand(new SwitchGameCommand(this, hexagonPrime));
    }
    private void addCommand(final Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    /**
     * Indicates whether the board game needs to be printed after each turn.
     * @return true if the game board needs to be printed after each turn, false otherwise.
     */
    public boolean isAutoPrint() {
        return autoPrint;
    }

    /**
     * Indicates wheter the second player is a AI Player.
     * @return true if the second player is a AI Player, false otherwise.
     */
    public boolean isAIPlayer() {
        return this.aIPlayer;
    }

    /**
     * Gets the ArtificialManager instance.
     * @return the ArtifialManager instance.
     */
    public ArtificialManager getArtificialManager() {
        return this.artificialManager;
    }
}
