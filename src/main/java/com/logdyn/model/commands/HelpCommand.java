package com.logdyn.model.commands;

import org.apache.log4j.Logger;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;


public class HelpCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(HelpCommand.class);
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
                Thread.sleep(500);
            } catch (final IOException e) {
                LOGGER.error("Error opening browser", e);
            } catch (final InterruptedException e) {
                LOGGER.error("Thread was unexpectedly interrupted", e);
                Thread.currentThread().interrupt();
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
