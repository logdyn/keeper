package com.logdyn.ui.console.commands;

public interface Command {
    public void execute(final String... args);
    public String getName();
}
