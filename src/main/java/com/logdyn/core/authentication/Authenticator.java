package com.logdyn.core.authentication;

import java.net.HttpURLConnection;

public interface Authenticator {
    void authenticate(final HttpURLConnection connection);
}
