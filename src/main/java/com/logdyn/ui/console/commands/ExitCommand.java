package com.logdyn.ui.console.commands;

import java.util.Arrays;
import java.util.Collection;

public class ExitCommand implements Command {
    @Override
    public void execute(final String... args) {
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getHelp() {
        return "Exits the program";
    }

    @Override
    public Collection<String> getNames() {
        return Arrays.asList("exit", "-x");
    }
}
