package com.logdyn.ui.console.commands;

import java.util.Collection;

public interface Command {
    public void execute(final String... args);
    public Collection<String> getNames();
    public String getDescription();
    public String getHelp();
}
