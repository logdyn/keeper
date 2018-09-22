package com.logdyn.ui.console.commands.task;

import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(name ="task", description = "List, add, or remove tasks", subcommands = HelpCommand.class)
public class TaskCommand extends CliCommand {}
