package com.logdyn;

import com.logdyn.core.persistence.StorageController;
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
import picocli.CommandLine.RunLast;

import java.util.List;

public class Main {

    private static Logger LOGGER = Logger.getLogger(Main.class);
    private static boolean isFirstRun = true;

    public static void main(final String... args) {
        if (isFirstRun) {
            isFirstRun = false;
            StorageController.load();
            Runtime.getRuntime().addShutdownHook(new Thread(StorageController::save, "Shutdown Save Hook"));
        }
        final CommandLine commandLine = new CommandLine(new KeeperCommand())
                .addSubcommand("repository", new CommandLine(new RepositoryCommand())
                        .addSubcommand("add", new RepositoryAddCommand(), "-a")
                        .addSubcommand("remove", new RepositoryRemoveCommand(), "-r")
                        .addSubcommand("list", new RepositoryListCommand(), "-l"),
                        "repos")
                .addSubcommand("exit", new ExitCommand());
        commandLine.parseWithHandlers(new RunLast(), new CliExceptionHandler(), args);
        StorageController.save();
    }

    //! TODO needs to return something identifiable
    private static class CliExceptionHandler implements IExceptionHandler2<List<Object>> {
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

