package com.logdyn.model;

import com.logdyn.model.repositories.Repository;

import java.util.Collection;

public class State {
    private String version;
    private Collection<Repository> repositories;

    public State(final String version, final Collection<Repository> repositories) {
        this.version = version;
        this.repositories = repositories;
    }

    public String getVersion() {
        return version;
    }

    public Collection<Repository> getRepositories() {
        return repositories;
    }
}
