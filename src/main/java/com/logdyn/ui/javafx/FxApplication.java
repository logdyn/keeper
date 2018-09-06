package com.logdyn.ui.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class FxApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(FxApplication.class);

    @Override
    public void start(final Stage primaryStage) throws Exception {
        LOGGER.debug("Starting JavaFX Application...");
        this.stop();
    }
}
