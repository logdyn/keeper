package com.logdyn.model.auth;

import java.net.URLConnection;

public interface Authenticator {
    void authenticate(final URLConnection connection);
}
