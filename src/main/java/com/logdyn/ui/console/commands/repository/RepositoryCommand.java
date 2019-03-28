package com.logdyn.ui.console.commands.repository;

import com.logdyn.ui.console.commands.CliCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Component
@Command(name = "repository",
        description = "List, add, or rename repositories",
        aliases = "repo",
        subcommands = {
                RepositoryAddCommand.class,
                RepositoryRemoveCommand.class,
                RepositoryListCommand.class,
                HelpCommand.class})
public class RepositoryCommand extends CliCommand
{
}
