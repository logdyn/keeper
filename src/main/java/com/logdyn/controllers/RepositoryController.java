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

    public static Optional<Repository> addRepository(final URL url){
        return addRepository(url, null);
    }

    public static Optional<Repository> addRepository(final URL url, final Authenticator auth) {
        final Optional<Repository> repo = factories.stream()
                .map(factory -> factory.createRepository(url, auth))
                .filter(Optional::isPresent)
                .findAny()
                .map(Optional::get);

        repo.ifPresent(repositories::add);
        return repo;
    }

    public static boolean removeRepository(final Repository repository)
    {
        return repositories.remove(repository);
    }

    public static Optional<Repository> getRepository(final URL url) {
        return Optional.ofNullable(repositories.stream()
                .filter(repository -> repository.isUrlMatch(url))
                .findAny()
                .orElseGet(() -> addRepository(url).orElse(null)));
    }

    public static Optional<Repository> getRepository(final String name) {
        return repositories.stream()
                .filter(repository -> repository.getName().equals(name))
                .findAny();
    }

    public static Optional<Task> getTask(final URL url) throws AuthenticationRequiredException {
        Optional<Repository> repo = getRepository(url);
        if (repo.isPresent()) {
            return repo.get().getTask(url);
        }
        return Optional.empty();
    }

    public static Optional<Task> getTask(final String repoName, final String taskName) throws AuthenticationRequiredException {
        Optional<Repository> repository = getRepository(repoName);
        if (repository.isPresent()) {
            return repository.get().getTask(taskName);
        }
        return Optional.empty();
    }
}
