package com.logdyn.model.repositories;

import com.logdyn.model.Task;

import java.net.URL;
import java.util.Optional;

public class JiraRepository implements Repository {
    private String name;
    private URL url;

    public JiraRepository(final URL url, final String name) {
        this.url = url;
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public boolean isUrlMatch(final URL url) {
        return this.url.getHost().equals(url.getHost());
    }

    @Override
    public Optional<Task> getTask(final URL url) {
        return Optional.empty();
    }

    @Override
    public Optional<Task> getTask(final String id) {
        return Optional.empty();
    }
}
