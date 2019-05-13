package com.logdyn.ui.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class FxApplication extends Application
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FxApplication.class);

    private static ConfigurableApplicationContext context;

    public static void setContext(final ConfigurableApplicationContext context)
    {
        FxApplication.context = context;
    }

    @Override
    public void init()
    {
        FxApplication.context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        LOGGER.debug("Starting JavaFX Application...");
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);

        final Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("/fxml/main.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Keeper");
        primaryStage.show();
    }

    @Override
    public void stop()
    {
        FxApplication.context.stop();
    }
}
