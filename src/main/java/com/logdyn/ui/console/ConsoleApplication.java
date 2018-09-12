package com.logdyn.ui.console;

import com.logdyn.ui.console.commands.ExitCommand;
import com.logdyn.ui.console.commands.KeeperCommand;
import com.logdyn.ui.console.commands.repository.RepositoryCommand;
import com.logdyn.ui.console.commands.repository.RepositoryListCommand;
import picocli.CommandLine;
import picocli.CommandLine.RunAll;

public class ConsoleApplication {
    public static void main(final String... args) {
        CommandLine commandLine = new CommandLine(new KeeperCommand())
                .addSubcommand("repository", new CommandLine(new RepositoryCommand())
                        .addSubcommand("list", new RepositoryListCommand(), "-l"),
                        "repos")
                .addSubcommand("exit", new ExitCommand());
        commandLine.parseWithHandler(new RunAll(), args);
    }
}
