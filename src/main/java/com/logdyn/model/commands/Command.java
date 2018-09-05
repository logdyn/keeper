package com.logdyn.model.commands;

public interface Command {
    public void execute(final String... args);
    public String getName();
}
