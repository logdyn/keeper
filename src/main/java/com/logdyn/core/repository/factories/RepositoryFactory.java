package com.logdyn.core.repository.factories;

import com.logdyn.core.authentication.Authenticator;
import com.logdyn.core.repository.Repository;

import java.net.URL;
import java.util.Optional;

public interface RepositoryFactory {
    Optional<Repository> createRepository(final URL url);

    Optional<Repository> createRepository(final URL url, final Authenticator auth);
}
