package com.logdyn.ui.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class providing helpful methods for interacting with the console.
 *
 * e.g. prompting the user for input.
 */
public final class ConsoleUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ConsoleUtils.class);

    private ConsoleUtils() {throw new AssertionError();}

    /**
     * Prompts the user for a Yes/No response in the command line. Options must be "y" or "n". If no input, returns false.
     * <br\>
     * Will continue to prompt until a recognised input is entered.
     * @param message The prompt to display to the user.
     * @param args    Arguments referenced by the format specifiers in the format string.
     * @return {@code true} when "y" is entered, {@code false} when "n" is entered or no input
     */
    public static boolean promptBoolean(final String message, final Object... args) {
        return promptBoolean(false, message, args);
    }

    /**
     * Prompts the user for a Yes/No response in the command line. Options must be "y" or "n". If no input, returns {@code defaultResult}.
     * <br\>
     * Will continue to prompt until a recognised input is entered.
     * @param message The prompt to display to the user.
     * @param defaultResult The result to return if no input is entered.
     * @param args    Arguments referenced by the format specifiers in the format string.
     * @return {@code true} when "y" is entered, {@code false} when "n" is entered, {@code defaultResult} if no input
     */
    public static boolean promptBoolean(final boolean defaultResult, final String message, final Object... args) {

        final char yesOption = defaultResult ? 'Y' : 'y';
        final char noOption = defaultResult ? 'n' : 'N';
        final String boolOption = "[" + yesOption + "|" + noOption + "]\n";
        Boolean result = null;

        while (result == null) {
            final String input = prompt(message + boolOption, args);
            if (input.isEmpty()) {
                result = defaultResult;
            } else if (input.equalsIgnoreCase("y")) {
                result = true;
            } else if (input.equalsIgnoreCase("n")) {
                result = false;
            }
        }

        return result;
    }


    /**
     * Prompts the user for a response in the command line.
     * <br \>
     * Uses {@link java.io.PrintStream#printf(String, Object...)}
     *
     * @param message The prompt to display to the user.
     * @param args    Arguments referenced by the format specifiers in the format string.
     * @return The next line of user input, or an empty string if an IOException is thrown while reading from System.in
     * @see java.io.PrintStream#printf(String, Object...)
     */
    public static String prompt(final String message, final Object... args) {
        System.out.printf(message, args);
        return prompt();
    }

    /**
     * Prompts the user for a response in the command line.
     *
     * @return The next line of user input, or an empty string if an IOException is thrown while reading from System.in
     */
    public static String prompt() {
        System.out.print("> ");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (final IOException e) {
            LOGGER.error("Error reading from System.in", e);
            return "";
        }
    }
}
