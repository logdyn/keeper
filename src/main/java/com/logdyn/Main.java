package com.logdyn;


import com.logdyn.core.authentication.ssl.InteractiveTrustManager;
import com.logdyn.core.persistence.StorageController;
import com.logdyn.ui.console.commands.ExitCommand;
import com.logdyn.ui.console.commands.KeeperCommand;
import com.logdyn.ui.console.commands.repository.RepositoryAddCommand;
import com.logdyn.ui.console.commands.repository.RepositoryCommand;
import com.logdyn.ui.console.commands.repository.RepositoryListCommand;
import com.logdyn.ui.console.commands.repository.RepositoryRemoveCommand;
import com.logdyn.ui.javafx.FxApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.IExceptionHandler2;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.RunLast;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner
{
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private KeeperCommand keeperCommand;

    @Autowired
    private StorageController storageController;

    public static void main(final String... args)
    {
        final SpringApplication springApplication = new SpringApplication(Main.class);
        springApplication.setLogStartupInfo(false);
        springApplication.run(args);
    }

    @PostConstruct
    public void init() throws KeyManagementException, NoSuchAlgorithmException
    {
        addSSLVerifier(new InteractiveTrustManager());
        storageController.load();
        Runtime.getRuntime().addShutdownHook(new Thread(storageController::save, "Shutdown Save Hook"));
    }

    @Override
    public void run(final String... args)
    {
        new CommandLine(keeperCommand, context::getBean)
                .parseWithHandlers(new RunLast(), new CliExceptionHandler(), args);
    }

    private static void addSSLVerifier(final TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustManager}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
