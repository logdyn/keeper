package com.logdyn.application;

import com.logdyn.model.commands.Command;
import com.logdyn.model.commands.ExitCommand;
import com.logdyn.model.commands.HelpCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class App {

    private static final Collection<Command> commands = new ArrayList<>();
    private static Command HELP_COMMAND = new HelpCommand();
    private static String VERSION = "0.5.0";

    static {
        //TODO add commands
        commands.add(HELP_COMMAND);
        commands.add(new ExitCommand());
    }

    public static void main(final String... args) {

        System.out.println(String.format("Keeper: v%s", VERSION));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String line = reader.readLine();
                final String[] split = line.split("\\s");
                commands.stream()
                        .filter(command -> command.getName().equalsIgnoreCase(split[0]))
                        .findAny()
                        .orElse(HELP_COMMAND)
                        .execute(Arrays.copyOfRange(split, 1, split.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //? TODO save and exit?
        }
    }
}
