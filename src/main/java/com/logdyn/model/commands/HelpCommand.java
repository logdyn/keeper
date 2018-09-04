package com.logdyn.model.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

public class HelpCommand implements Command {
    private static final String HELP_URL = "http://github.com/logdyn/keeper/wiki";
    private final Collection<Command> commands;

    public HelpCommand(final Collection<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(final String... args) {
        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                System.out.println("Opening help page in browser...");
                Desktop.getDesktop().browse(URI.create(HELP_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(String.format("Help page can be found at %s", HELP_URL));
        }
    }

    @Override
    public String getName() {
        return "help";
    }
}
