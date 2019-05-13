package com.logdyn.core.repository;

import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.repository.factories.RepositoryFactory;
import com.logdyn.core.task.Task;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
public class RepositoryController {
    private final Collection<RepositoryFactory<? extends Repository>> factories;
    private final Collection<Repository> repositories = new HashSet<>();

    public RepositoryController(final Collection<RepositoryFactory<? extends Repository>> factories)
    {
        this.factories = factories;
    }

    public Optional<? extends Repository> addRepository(final URL url){
        return addRepository(url, null);
    }

    public Optional<? extends Repository> addRepository(final URL url, final String name) {
        final Optional<Repository> repo = factories.stream()
                .map(factory -> factory.createRepository(url, name))
                .filter(Optional::isPresent)
                .findAny()
                .map(Optional::get);

        return repo.filter(repositories::add);
    }

    public boolean removeRepository(final Repository repository)
    {
        return repositories.remove(repository);
    }

    public Optional<Repository> getRepository(final URL url) {
        return repositories.stream()
                .filter(repository -> repository.isUrlMatch(url))
                .findAny()
                .or(() -> addRepository(url));
    }

    public Optional<Repository> getRepository(final String name) {
        return repositories.stream()
                .filter(repository -> repository.getName().equals(name))
                .findAny();
    }

    public Collection<Repository> getRepositories() {
        return Collections.unmodifiableCollection(repositories);
    }

    public Optional<Task> getTask(final URL url) throws AuthenticationRequiredException {
        Optional<Repository> repo = getRepository(url);
        if (repo.isPresent()) {
            return repo.get().getTask(url);
        }
        return Optional.empty();
    }

    public Optional<Task> getTask(final String repoName, final String taskName) throws AuthenticationRequiredException {
        Optional<Repository> repository = getRepository(repoName);
        if (repository.isPresent()) {
            return repository.get().getTask(taskName);
        }
        return Optional.empty();
    }

    public boolean addRepositories(final Collection<Repository> repositories) {
        if (repositories != null) {
            return this.repositories.addAll(repositories);
        }

        return false;
    }

    public boolean addRepository(final Repository repo){
        return repositories.add(repo);
    }
}
