package com.logdyn.ui.console;

import com.logdyn.SystemConfig;
import com.logdyn.ui.console.commands.Command;
import com.logdyn.ui.console.commands.DefaultCommand;
import com.logdyn.ui.console.commands.ExitCommand;
import com.logdyn.ui.console.commands.HelpCommand;
import com.logdyn.ui.console.commands.VersionCommand;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ConsoleApplication {

    private static final Logger LOGGER = Logger.getLogger(ConsoleApplication.class);
    private static final Collection<Command> commands = new ArrayList<>();
    private static HelpCommand HELP_COMMAND = new HelpCommand(commands);

    static {
        //TODO add commands
        commands.add(HELP_COMMAND);
        commands.add(new ExitCommand());
        commands.add(new VersionCommand());
    }

    public static void main(final String... args) {
        if (args[0].equals("-i")) {
            interactive();
        } else {
            execute(args);
        }
    }

    private static void interactive() {
        System.out.printf("Keeper: %s%n", SystemConfig.VERSION);
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
        //If second argument is help
        if (args.length > 1 && HELP_COMMAND.getNames().contains(args[1].toLowerCase())) {
            HELP_COMMAND.execute(args);
        } else {
            commands.stream()
                    .filter(command -> command.getNames().contains(args[0].toLowerCase()))
                    .findAny()
                    .orElseGet(() -> new DefaultCommand(args[0]))
                    .execute(Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
