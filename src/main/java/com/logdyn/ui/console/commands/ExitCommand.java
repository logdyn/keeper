package com.logdyn.ui.console.commands;

public class ExitCommand implements Command {
    @Override
    public void execute(final String... args) {
        System.exit(0);
    }

    @Override
    public String getName() {
        return "exit";
    }
}
