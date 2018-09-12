package com.logdyn.ui.console.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(mixinStandardHelpOptions = true, versionProvider = com.logdyn.SystemConfig.class)
public abstract class CliCommand implements Runnable {}
