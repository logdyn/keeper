package com.logdyn.model.repositories;

import com.logdyn.model.Task;
import com.logdyn.model.auth.AuthenticationRequiredException;
import com.logdyn.model.auth.Authenticator;

import java.net.URL;
import java.util.Optional;

public interface Repository {
    String getName();
    URL getUrl();
    void setAuthenticator(final Authenticator auth);
    boolean isUrlMatch(final URL url);
    Optional<Task> getTask(final URL url) throws AuthenticationRequiredException;
    Optional<Task> getTask(final String id) throws AuthenticationRequiredException;
    void submitTask(final Task task) throws AuthenticationRequiredException;
}
