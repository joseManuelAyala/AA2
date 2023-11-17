/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */
package kastel.ui;

import java.util.Objects;
/*
    -- META DISCLAIMER --
    Note that this class main porpose is to show a level of abstraction for the command pattern. It is here not 
    neccessarily needed since there is no command in this example solution that is not a SantoriniCommand. For 
    different use cases, this class might be useful though, one example can be if you consider multiple instances of
    the game running at the same time.
 */

/**
 * This class represents a basic command that can be executed by the user.
 * This class was taken from the solution for ÜB 5 from SS 23.
 * @author Programmieren-Team
 */
public abstract class Command {

    /*
        -- META DISCLAIMER --
        Viewing only this exercise, this commandHandler field could be set into SantoriniCommand since there is no use
        of it in any command not being a SantoriniCommand. It is kept here anyway since this class represents a more
        basic command, not neccessarily related to the Santorini game. There might be use cases in which you want to
        have the commandHandler accessible like this without the context of the game. 
     */
    /**
     * The command handler.
     */
    protected final CommandHandler commandHandler;
    private final String commandName;

    /**
     * Constructs a new Command.
     *
     * @param commandName    the name of the command
     * @param commandHandler the command handler
     */
    protected Command(final String commandName, final CommandHandler commandHandler) {
        this.commandName = Objects.requireNonNull(commandName);
        this.commandHandler = Objects.requireNonNull(commandHandler);
    }

    /**
     * Returns the name of the command.
     *
     * @return the name of the command
     */
    public final String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param commandArguments the arguments of the command
     */
    public abstract void execute(String[] commandArguments);

}
