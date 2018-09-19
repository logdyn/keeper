package com.logdyn.ui.console.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(versionProvider = com.logdyn.SystemConfig.class)
public abstract class CliCommand implements Runnable {
    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Displays this help message")
    private boolean helpRequested = false;

    @Spec
    private CommandSpec spec;

    @Override
    public void run() {
        this.spec.commandLine().usage(System.out);
    }
}
