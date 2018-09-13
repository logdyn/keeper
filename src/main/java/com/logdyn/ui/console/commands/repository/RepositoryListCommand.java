package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Command(name = "list", description = "Lists repositories")
public class RepositoryListCommand extends CliCommand {

    @Option(names = "-n", description = "Get all repositories with matching name", paramLabel = "REPOSITORY_NAME")
    private List<String> repositoryName = new ArrayList<>();

    @Option(names = "-u", description = "Get all repositories with matching URL", paramLabel = "REPOSITORY_URL")
    private List<URL> repositoryUrl = new ArrayList<>();

    @Override
    public void run() {

        final Collection<Repository> repos;
        if (repositoryName.isEmpty() && repositoryUrl.isEmpty()) {
            repos = RepositoryController.getRepositories();
        }
        else{
            repos = new ArrayList<>(repositoryName.size() + repositoryUrl.size());
            repositoryName.stream()
                    .map(RepositoryController::getRepository)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(repos::add);
            repositoryUrl.stream()
                    .map(RepositoryController::getRepository)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(repos::add);
        }
        repos.forEach(System.out::println);
    }
}