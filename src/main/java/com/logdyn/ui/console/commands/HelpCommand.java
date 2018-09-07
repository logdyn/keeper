package com.logdyn.ui.console.commands;

import org.apache.log4j.Logger;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


public class HelpCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(HelpCommand.class);
    private static final String HELP_URL = "http://github.com/logdyn/keeper/wiki";
    private final Collection<Command> commands;

    public HelpCommand(final Collection<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(final String... args) {
        if (args.length == 0) {
            this.commands.forEach(this::printHelp);
        } else if (args[0].equalsIgnoreCase("web")) {
            openBrowser();
        } else {
            final Optional<Command> command = this.commands.stream()
                    .filter(c -> c.getNames().contains(args[0].toLowerCase()))
                    .findAny();
            if (command.isPresent())
            {
                command.ifPresent(this::printHelp);
            }
            else
            {
                DefaultCommand.execute(args[0]);
            }
        }
    }

    private void printHelp(final Command command){
        //e.g. [help | --help | /?] [<command>] - prints help text
        System.out.printf("[%s] %s - %s%n", String.join(" | ", command.getNames()), command.getDescription(), command.getHelp());
    }

    private void openBrowser()
    {
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
            System.out.printf("Help page can be found at %s%n", HELP_URL);
        }
    }

    @Override
    public Collection<String> getNames() {
        return Arrays.asList("help", "--help", "/?");
    }

    @Override
    public String getDescription() {
        return "[<command>]";
    }

    @Override
    public String getHelp() {
        return "prints help text";
    }
}
