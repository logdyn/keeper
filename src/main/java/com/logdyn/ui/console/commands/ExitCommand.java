package com.logdyn.ui.console.commands;

import picocli.CommandLine.Command;

@Command(description = "Exits the keeper command line", name = "exit")
public class ExitCommand extends CliCommand {
    @Override
    public void run() {
        System.exit(0);
    }
}
