package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Command(name = "Remove", description = "Removes any repositories matching the given names or URLs")
public class RepositoryRemoveCommand extends CliCommand {

    @Option(names = "-n", description = "Removes all repositories with matching name", paramLabel = "REPOSITORY_NAME")
    private List<String> repositoryName = new ArrayList<>();

    @Option(names = "-u", description = "Remove all repositories with matching URL", paramLabel = "REPOSITORY_URL")
    private List<URL> repositoryUrl = new ArrayList<>();

    @Option(names = {"-a", "--all"}, description = "Removes all repositories")
    private boolean all;

    @Override
    public void run() {

        final Collection<Repository> repos;
        if (all) {
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
        final Reader reader = new InputStreamReader(System.in);
        final long removedCount = repos.stream()
                .filter(repository -> {
                    System.out.printf("Remove repository '%s' @ %s? [Y|n]%n", repository.getName(), repository.getUrl());
                    try {
                        final char result = (char) reader.read();
                        return result == 'y' || result == 'Y';
                    } catch (final IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .peek(RepositoryController::removeRepository)
                .count();
        System.out.printf("Removed %d repositories%n", removedCount);
    }
}
