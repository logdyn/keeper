package com.logdyn.application;

import com.logdyn.model.commands.Command;
import com.logdyn.model.commands.ExitCommand;
import com.logdyn.model.commands.HelpCommand;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class App {

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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                final String[] args = reader.readLine().split("\\s");
                execute(args);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //? TODO save and exit?
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
