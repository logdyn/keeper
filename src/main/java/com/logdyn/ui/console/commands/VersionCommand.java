package com.logdyn.ui.console.commands;

import com.logdyn.SystemConfig;

import java.util.Arrays;
import java.util.Collection;

public class VersionCommand implements Command {
    @Override
    public void execute(final String... args) {
        System.out.println(SystemConfig.VERSION);
    }

    @Override
    public Collection<String> getNames() {
        return Arrays.asList("version", "--version", "-v");
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getHelp() {
        return "Prints the version of the application";
    }
}
