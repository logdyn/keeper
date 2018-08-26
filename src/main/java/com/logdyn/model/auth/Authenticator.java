package com.logdyn.model.auth;

import java.net.HttpURLConnection;

public interface Authenticator {
    void authenticate(final HttpURLConnection connection);
}
