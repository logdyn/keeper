package com.logdyn.core.authentication.ssl;

import com.logdyn.ui.console.ConsoleUtils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

public class InteractiveTrustManager implements X509TrustManager {

    private final Collection<X509Certificate> acceptedCerts = new ArrayList<>();

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        if (!acceptedCerts.contains(chain[0])) {
            final boolean userResponse = ConsoleUtils.promptBoolean(
                    true,
                    "SSL Certificate could not be verified.%nDo you want to connect to server with certificate '%s'?%n",
                    chain[0].getSubjectX500Principal().getName());

            if (userResponse) {
                acceptedCerts.add(chain[0]);
            } else {
                throw new CertificateException();
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
