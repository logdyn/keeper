package com.logdyn.ui.console.commands;

import com.logdyn.SystemConfig;
import com.logdyn.ui.console.ConsoleApplication;
import org.apache.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Command(name = "keeper",
        description = "Entry point for the Keeper command line")
public class KeeperCommand extends CliCommand {
    private static final Logger LOGGER = Logger.getLogger(KeeperCommand.class);

    private static boolean INTERACTIVE_MODE;

    @Option(names={"-i", "--interactive"}, description = "Run keeper in interactive mode")
    private boolean interactiveFlag = false;

    @Override
    public void run() {
        if (interactiveFlag && !INTERACTIVE_MODE) {
            INTERACTIVE_MODE = true;
            System.out.printf("Keeper: %s%n", SystemConfig.VERSION);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                try{
                    System.out.print("> ");
                    final String[] args = reader.readLine().split("\\s");
                    ConsoleApplication.main(args);
                } catch (final Exception e) {
                    LOGGER.error(e);
                }
            }
        }
    }
}
