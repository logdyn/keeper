package com.logdyn.ui.console.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(description = "Entry point for the Keeper command line", name = "keeper", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class}, version = "Keeper version: Dev")
public class KeeperCommand implements Runnable {

    @Override
    public void run() {
        //NOOP
    }
}
