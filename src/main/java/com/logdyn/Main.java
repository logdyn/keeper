package com.logdyn;

import com.logdyn.core.authentication.ssl.InteractiveTrustManager;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class);
    private static boolean isFirstRun = true;

    public static void main(final String... args) throws Exception{
        if (isFirstRun) {
            isFirstRun = false;
            addSSLVerifier(new InteractiveTrustManager());
        }
        final CommandLine commandLine = new CommandLine(new KeeperCommand())
                .addSubcommand("repository", new CommandLine(new RepositoryCommand())
                                .addSubcommand("add", new RepositoryAddCommand(), "-a")
                                .addSubcommand("remove", new RepositoryRemoveCommand(), "-r")
                                .addSubcommand("list", new RepositoryListCommand(), "-l"),
                        "repos")
                .addSubcommand("exit", new ExitCommand());
        commandLine.parseWithHandlers(new RunLast(), new CliExceptionHandler(), args);
    }

    public static void addSSLVerifier (final TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustManager}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
