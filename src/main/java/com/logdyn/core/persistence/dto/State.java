package com.logdyn.core.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.logdyn.core.repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@JsonRootName("state")
public class State
{
    @JacksonXmlProperty(isAttribute = true)
    private int version;

    @JacksonXmlElementWrapper(localName = "repositories")
    @JsonProperty("repository")
    private Collection<Repository> repositories;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date time;

    private State()
    {
        this(new ArrayList<>(), 0);
    }

    public State(final Collection<Repository> repositories, final int version)
    {
        this(repositories, version, null);
    }

    public State(final Collection<Repository> repositories, final int version, final Date time)
    {
        this.version = version;
        this.repositories = repositories;
        this.time = time;
    }

    public int getVersion()
    {
        return this.version;
    }

    public Date getTime()
    {
        return this.time;
    }

    public Collection<Repository> getRepositories()
    {
        return this.repositories;
    }
}
