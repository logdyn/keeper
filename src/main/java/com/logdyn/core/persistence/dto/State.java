package com.logdyn.core.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.logdyn.core.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;

@JsonRootName("state")
public class State
{
    @JacksonXmlProperty(isAttribute = true)
    private int version;

    @JacksonXmlElementWrapper(localName = "repositories")
    @JsonProperty("repository")
    private Collection<Repository> repositories;

    private State()
    {
        this(0, new ArrayList<>());
    }

    public State(final int version, final Collection<Repository> repositories)
    {
        this.version = version;
        this.repositories = repositories;
    }

    public int getVersion()
    {
        return this.version;
    }

    public Collection<Repository> getRepositories()
    {
        return this.repositories;
    }
}
