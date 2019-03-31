package com.logdyn.core.repository.factories;

import com.logdyn.core.repository.Repository;

import java.net.URL;
import java.util.Optional;

public interface RepositoryFactory<T extends Repository> {
    Optional<T> createRepository(final URL url);

    Optional<T> createRepository(final URL url, final String name);

    T instantiateRepository(final URL url, final String name);
}
