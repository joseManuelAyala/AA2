/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package kastel.ui;

/**
 * This class describes a result of a command execution.
 * This class was taken from the solution for ÜB 5 from SS 23.
 * @author Programmieren-Team
 */
public class Result {
    /**
     * The type indicating the outcome of the command.
     */
    private final ResultType type;
    /**
     * A message associated with the result.
     */
    private final String message;
    /**
     * Indicates whether the result prompst printing of the commamd input.
     */
    private final boolean commandPrint;
    /**
     * Inidicates whether a message about the current turn should be printed.
     */
    private final boolean printTurnsMessage;

    /**
     * constructs a new Result whithout a message.
     * @param type the type of result.
     * @param autoPrint indicates wether the game board needs to be printed.
     * @param printTurns indicates whehter the players turns needs to be printed.
     */
    public Result(final ResultType type, boolean autoPrint, boolean printTurns) {
        this(type, null, autoPrint, printTurns);
    }


    /**
     * Constructs a new Result with message.
     *
     * @param type the type of the result.
     * @param message message to carry
     * @param autoPrint indicates whter the game board needs to be printed.
     * @param printTurns indicates whehter the players turns needs to be printed.
     */
    public Result(ResultType type, String message, boolean autoPrint, boolean printTurns) {
        this.commandPrint = autoPrint;
        this.type = type;
        this.message = message;
        this.printTurnsMessage = printTurns;
    }

    /**
     * Returns the type of result.
     *
     * @return the type of result.
     */
    public ResultType getType() {
        return this.type;
    }

    /**
     * Returns the carried message of the result or {@code null} if there is none.
     *
     * @return the message or {@code null}
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Indicates whether the board game needs to be printed.
     * @return true if the game board needs to be printed, false otherwise.
     */
    public boolean isCommandPrint() {
        return this.commandPrint;
    }

    /**
     * Indicates wheter the players turns need to be printed.
     * @return true if the players turn needs to be printed.
     */
    public boolean isPrintTurnsMessage() {
        return printTurnsMessage;
    }
}
