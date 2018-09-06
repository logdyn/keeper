package com.logdyn.core.authentication;

import java.net.HttpURLConnection;
import java.util.Base64;

public class BasicAuthenticator implements Authenticator {

    private static final String BASIC_AUTH_KEY = "Authorization"; //NON-NLS
    private static final String BASIC_AUTH_PREFIX = "Basic "; //NON-NLS
    private final String headerValue;

    public BasicAuthenticator(final String username, final String password) {
        this.headerValue = BASIC_AUTH_PREFIX + Base64.getEncoder().encodeToString((username + ':' + password).getBytes());
    }

    @Override
    public void authenticate(final HttpURLConnection conn) {
        conn.setRequestProperty(BASIC_AUTH_KEY, this.headerValue);
    }
}
