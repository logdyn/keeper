package com.logdyn.model.repositories;

import com.logdyn.model.Task;

import java.net.URL;
import java.util.Optional;

public interface Repository {
    String getName();
    URL getUrl();
    boolean isUrlMatch(final URL url);
    Optional<Task> getTask(final URL url);
    Optional<Task> getTask(final String id);
}
