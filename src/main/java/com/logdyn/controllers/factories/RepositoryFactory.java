package com.logdyn.controllers.factories;

import com.logdyn.model.auth.Authenticator;
import com.logdyn.model.repositories.Repository;

import java.net.URL;
import java.util.Optional;

public interface RepositoryFactory {
    Optional<Repository> createRepository(final URL url);

    Optional<Repository> createRepository(final URL url, final Authenticator auth);
}
