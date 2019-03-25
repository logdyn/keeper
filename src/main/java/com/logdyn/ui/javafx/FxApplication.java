package com.logdyn.ui.javafx;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class FxApplication extends Application
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FxApplication.class);

    @Override
    public void start(final Stage primaryStage) throws Exception {
        LOGGER.debug("Starting JavaFX Application...");
        new Alert(AlertType.INFORMATION, "blarg").showAndWait();
        this.stop();
    }
}
