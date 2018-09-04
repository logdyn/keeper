package com.logdyn.model.commands;

public class HelpCommand implements Command {

    @Override
    public void execute(final String... args) {
        System.out.println("AHHHH HELP");
    }

    @Override
    public String getName() {
        return "HELP";
    }
}
