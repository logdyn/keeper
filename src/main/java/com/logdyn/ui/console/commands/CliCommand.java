package com.logdyn.ui.console.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(versionProvider = com.logdyn.SystemConfig.class)
public abstract class CliCommand implements Runnable {
    @Option(names = { "-h", "--help" }, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;
}
