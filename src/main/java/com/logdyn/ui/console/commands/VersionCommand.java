package com.logdyn.ui.console.commands;

import com.logdyn.SystemConfig;

public class VersionCommand implements Command {
    @Override
    public void execute(final String... args) {
        System.out.println(SystemConfig.VERSION);
    }

    @Override
    public String getName() {
        return "version";
    }
}
