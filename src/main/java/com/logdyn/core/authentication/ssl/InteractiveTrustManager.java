package com.logdyn.core.authentication.ssl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class InteractiveTrustManager implements X509TrustManager {

    private final Collection<X509Certificate> acceptedCerts = new ArrayList<>();

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) { }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        if (!acceptedCerts.contains(chain[0])) {
            System.out.printf("SSL Certificate could not be verified.%nDo you want to connect to server with certificate '%s'?%n[Y|n]%n> ", chain[0].getSubjectX500Principal().getName());
            Scanner scanner = new Scanner(System.in);

            if (scanner.nextLine().toLowerCase().charAt(0) != 'y') {

                throw new CertificateException();
            }
            acceptedCerts.add(chain[0]);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
