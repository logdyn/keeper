package com.logdyn.core.authentication.ssl;

import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
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
            System.out.printf("SSL Certificate could not be verified.%nDo you want to connect to server with certificate '%s'?%n[Y|n]%n> ",
                    chain[0].getSubjectX500Principal().getName());

            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                final String nextLine = reader.readLine();

                if (nextLine.isEmpty() || nextLine.equalsIgnoreCase("y")) {
                    acceptedCerts.add(chain[0]);
                } else {
                    throw new CertificateException();
                }
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
