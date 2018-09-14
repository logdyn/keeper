package com.logdyn;

import com.logdyn.core.authentication.ssl.InteractiveTrustManager;
import com.logdyn.ui.console.ConsoleApplication;
import com.logdyn.ui.javafx.FxApplication;
import javafx.application.Application;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(final String... args) throws Exception {
        if (args.length == 0) {
            Application.launch(FxApplication.class, args);
        } else {
            addSSLVerifier(new InteractiveTrustManager());
            ConsoleApplication.main(args);
        }
    }

    public static void addSSLVerifier (final TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustManager}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
