package com.logdyn.model.repositories;

import com.logdyn.model.Task;
import com.logdyn.model.auth.AuthenticationRequiredException;
import com.logdyn.model.auth.Authenticator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Repository {
    String name;
    URL url;
    Authenticator auth;
    private Map<String, Task> tasks = new HashMap<>();

    public Repository(final URL url, final String name) {
        this(url, name, null);
    }

    public Repository(final URL url, final String name, final Authenticator auth) {
        this.url = url;
        this.name = name;
        this.auth = auth;
    }

    public String getName() { return this.name; }
    public URL getUrl() { return this.url; }
    void setAuthenticator(final Authenticator auth) { this.auth = auth; }
    public boolean isUrlMatch(final URL url) { return this.url.getHost().equals(url.getHost()); }

    public Optional<Task> getTask(URL url) throws AuthenticationRequiredException {
        if (!isUrlMatch(url)){
            return Optional.empty();
        }
        return this.getTask(this.getTaskId(url));
    }

    public Optional<Task> getTask(String id) throws AuthenticationRequiredException {
        Optional<Task> task = Optional.ofNullable(tasks.get(id));
        if (!task.isPresent()) {
            task = this.getRemoteTask(id);
            task.ifPresent(t -> tasks.put(id, t));
        }
        return task;
    }

    abstract String getTaskId(final URL url);
    abstract void submitTask(final Task task) throws AuthenticationRequiredException;
    abstract Optional<Task> getRemoteTask(final String id) throws AuthenticationRequiredException;
}
