package com.logdyn.ui.console.commands.repository;

import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(name ="repository", description = "List, add, or remove repositories", subcommands = HelpCommand.class)
public class RepositoryCommand extends CliCommand {}
