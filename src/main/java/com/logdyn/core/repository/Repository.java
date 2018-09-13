package com.logdyn.core.repository;

import com.logdyn.core.task.Task;
import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.authentication.Authenticator;

import java.net.URL;
import java.util.Optional;

public interface Repository {
    String getName();
    void setName(String name);
    URL getUrl();
    void setAuthenticator(final Authenticator auth);
    boolean isUrlMatch(final URL url);
    Optional<Task> getTask(final URL url) throws AuthenticationRequiredException;
    Optional<Task> getTask(final String id) throws AuthenticationRequiredException;
    void submitWorkLog(final Task task) throws AuthenticationRequiredException;
}
