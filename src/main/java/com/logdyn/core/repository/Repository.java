package com.logdyn.core.repository;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.authentication.Authenticator;
import com.logdyn.core.task.Task;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({@JsonSubTypes.Type(JiraRepository.class)})
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

    public Authenticator getAuthenticator()
    {
        return this.auth;
    }

    public void setAuthenticator(final Authenticator auth) { this.auth = auth; }
    public boolean isUrlMatch(final URL url)
    {
        return this.url.getPort() == url.getPort() && this.url.getHost().equals(url.getHost());
    }

    public Optional<Task> getTask(final URL url) throws AuthenticationRequiredException {
        if (!isUrlMatch(url)){
            return Optional.empty();
        }
        return this.getTask(this.getTaskId(url));
    }

    public Optional<Task> getTask(final String id) throws AuthenticationRequiredException {
        Optional<Task> task = Optional.ofNullable(tasks.get(id));
        if (!task.isPresent()) {
            task = this.getRemoteTask(id);
            task.ifPresent(t -> tasks.put(id, t));
        }
        return task;
    }

    public void addTask(final Task task) {
        this.tasks.put(task.getId(), task);
    }

    abstract String getTaskId(final URL url);
    abstract void submitTask(final Task task) throws AuthenticationRequiredException;
    abstract Optional<Task> getRemoteTask(final String id) throws AuthenticationRequiredException;

    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    @JsonSetter("tasks")
    public void addTasks(final Collection<Task> tasks)
    {
        if (tasks != null)
        {
            tasks.forEach(this::addTask);
        }
    }

    public void setName(final String repositoryName) {
        this.name = repositoryName;
    }

    @Override
    public String toString() {
        return this.name + " <" +  this.url + '>';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Repository that = (Repository) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return 137 * url.hashCode();
    }
}
