package com.logdyn;

import com.logdyn.ui.javafx.FxApplication;
import com.logdyn.ui.console.commands.Command;
import com.logdyn.ui.console.commands.ExitCommand;
import com.logdyn.ui.console.commands.HelpCommand;
import javafx.application.Application;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    private static final Collection<Command> commands = new ArrayList<>();
    private static Command HELP_COMMAND = new HelpCommand(commands);
    private static String VERSION = "0.5.0";

    static {
        //TODO add commands
        commands.add(HELP_COMMAND);
        commands.add(new ExitCommand());
    }

    public static void main(final String... args) {
        if (args.length == 0) {
            Application.launch(FxApplication.class, args);
        } else if (args[0].equals("-i")) {
            interactive();
        } else {
            execute(args);
        }
    }

    private static void interactive() {
        System.out.println(String.format("Keeper: v%s", VERSION));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.print("> ");
                final String[] args = reader.readLine().split("\\s");
                execute(args);
            }
        } catch (final IOException e) {
            LOGGER.error("Error reading from system input", e);
        }
    }

    private static void execute(final String[] args) {
        commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(HELP_COMMAND)
                .execute(Arrays.copyOfRange(args, 1, args.length));
    }
}
