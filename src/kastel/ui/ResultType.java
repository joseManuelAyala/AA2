/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package kastel.ui;


/**
 * The type of Result of a execution.
 * This class was taken from the solution for ÜB 5 from SS 23.
 * @author Programmieren-Team
 */
public enum ResultType {

    /**
     * The execution did not end with success.
     */
    FAILURE() {
        @Override
        public <T> void printResult(final String formattedMessage, T... args) {
            System.err.printf("Error: " + formattedMessage + NEW_LINE_SYMBOL, args);
        }
    },

    /**
     * The execution did end with success.
     */
    SUCCESS() {
        @Override
        public <T> void printResult(final String formattedMessage, T... args) {
            System.out.printf(formattedMessage + NEW_LINE_SYMBOL, args);
        }
    };

    private static final String NEW_LINE_SYMBOL = "%n";

    /**
     * Prints the result of the execution.
     * @param formattedMessage  the formatted message
     * @param args              the arguments
     * @param <T>               the type of the arguments
     */
    public abstract <T> void printResult(String formattedMessage, T... args);
}
