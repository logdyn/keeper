package com.logdyn.ui.console.commands;

import com.logdyn.Main;
import com.logdyn.SystemConfig;
import com.logdyn.ui.console.ConsoleUtils;
import com.logdyn.ui.javafx.FxApplication;
import javafx.application.Application;
import org.apache.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "keeper",
        description = "Entry point for the Keeper command line", subcommands = HelpCommand.class)
public class KeeperCommand extends CliCommand {
    private static final Logger LOGGER = Logger.getLogger(KeeperCommand.class);

    private static boolean INTERACTIVE_MODE;

    @Option(names={"-i", "--interactive"}, description = "Run keeper in interactive mode")
    private boolean interactiveFlag = false;

    @Option(names = { "-V", "--version" }, versionHelp = true, description = "Displays the program version")
    private boolean versionRequested = false;

    @Option(names= {"-c"}, description = "Launch the application as a console application")
    private boolean cli;

    @Option (names={"-g"}, description = "Launch the application as a desktop application")
    private boolean gui;

    @Parameters
    private String[] args;

    @Override
    public void run() {
        if (interactiveFlag && !INTERACTIVE_MODE) {
            INTERACTIVE_MODE = true;
            System.out.printf("Keeper: %s%n", SystemConfig.VERSION);
            while (true){
                try{
                    Main.main(ConsoleUtils.prompt().split("\\s"));
                } catch (final Exception e) {
                    LOGGER.error(e);
                    System.out.println("Something went wrong: " + e.getLocalizedMessage());
                }
            }
        }
        else if (this.gui || (System.console() == null && !cli)) {
            Application.launch(FxApplication.class, this.args);
        }
        else {
            super.run();
        }
    }
}
