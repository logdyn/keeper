package com.logdyn.ui.console;

import com.logdyn.ui.console.commands.ExitCommand;
import com.logdyn.ui.console.commands.KeeperCommand;
import com.logdyn.ui.console.commands.repository.RepositoryAddCommand;
import com.logdyn.ui.console.commands.repository.RepositoryCommand;
import com.logdyn.ui.console.commands.repository.RepositoryListCommand;
import com.logdyn.ui.console.commands.repository.RepositoryRemoveCommand;
import org.apache.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.IExceptionHandler2;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.RunAll;

import java.util.List;

public class ConsoleApplication {

    private static Logger LOGGER = Logger.getLogger(ConsoleApplication.class);

    public static void main(final String... args) {
        CommandLine commandLine = new CommandLine(new KeeperCommand())
                .addSubcommand("repository", new CommandLine(new RepositoryCommand())
                        .addSubcommand("add", new RepositoryAddCommand(), "-a")
                        .addSubcommand("remove", new RepositoryRemoveCommand(), "-r")
                        .addSubcommand("list", new RepositoryListCommand(), "-l"),
                        "repos")
                .addSubcommand("exit", new ExitCommand());
        commandLine.parseWithHandlers(new RunAll(), new CliExceptionHandler(), args);
    }

    static class CliExceptionHandler implements IExceptionHandler2<List<Object>> {
        @Override
        public List<Object> handleParseException(final ParameterException ex, final String[] args) {
            LOGGER.debug(ex.getMessage(), ex);
            return null;
        }

        @Override
        public List<Object> handleExecutionException(final ExecutionException ex, final ParseResult parseResult) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

}

