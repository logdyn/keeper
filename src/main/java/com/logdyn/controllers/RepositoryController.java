package com.logdyn.controllers;

import com.logdyn.controllers.factories.JiraRepositoryFactory;
import com.logdyn.controllers.factories.RepositoryFactory;
import com.logdyn.model.auth.AuthenticationRequiredException;
import com.logdyn.model.auth.Authenticator;
import com.logdyn.model.repositories.Repository;
import com.logdyn.model.Task;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class RepositoryController {
    private static Collection<RepositoryFactory> factories = new LinkedList<>();
    private static Collection<Repository> repositories = new LinkedList<>();

    static {
        factories.add(new JiraRepositoryFactory());
    }

    public static boolean addRepository(final URL url){
        return addRepository(url, null);
    }

    public static boolean addRepository(final URL url, final Authenticator auth) {
        return factories.stream()
                .map(factory -> factory.createRepository(url, auth))
                .filter(Optional::isPresent)
                .findAny()
                .map(Optional::get)
                .map(repositories::add)
                .orElse(Boolean.FALSE);
    }

    public static boolean removeRepository(final Repository repository)
    {
        return repositories.remove(repository);
    }

    public static Optional<Repository> getRepository(final URL url) {
        return repositories.stream()
                .filter(repository -> repository.isUrlMatch(url))
                .findAny();
    }

    public static Optional<Task> getTask(final URL url) throws AuthenticationRequiredException {
        Optional<Repository> repo = getRepository(url);
        if (repo.isPresent()) {
            return repo.get().getTask(url);
        }
        return Optional.empty();
    }
}
