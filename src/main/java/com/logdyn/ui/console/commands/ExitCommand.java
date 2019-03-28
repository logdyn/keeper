package com.logdyn.ui.console.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(description = "Exits the keeper command line", name = "exit")
public class ExitCommand extends CliCommand {
    @Override
    public void run() {
        System.exit(0);
    }
}
