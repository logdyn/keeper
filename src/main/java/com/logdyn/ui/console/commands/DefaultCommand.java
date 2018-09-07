package com.logdyn.ui.console.commands;

import java.util.Collection;
import java.util.Collections;

public class DefaultCommand implements Command {

    private final String arg;

    public DefaultCommand(final String arg) {
        this.arg = arg;
    }

    @Override
    public void execute(final String... args) {
       DefaultCommand.execute(this.arg);
    }

    public static void execute(final String arg){
        System.out.printf("%s is not a recognised command.%n", arg);
    }

    @Override
    public Collection<String> getNames() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getHelp() {
        return "";
    }
}
