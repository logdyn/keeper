package com.logdyn;

import com.logdyn.ui.console.ConsoleApplication;
import com.logdyn.ui.javafx.FxApplication;
import javafx.application.Application;

public class Main {

    public static void main(final String... args) {
        if (args.length == 0) {
            Application.launch(FxApplication.class, args);
        } else {
            ConsoleApplication.main(args);
        }
    }
}
