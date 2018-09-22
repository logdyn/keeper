package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Command(name = "list", description = "Lists repositories")
public class RepositoryListCommand extends CliCommand {

    @Option(names = "-n", description = "Get all repositories with matching name", paramLabel = "REPOSITORY_NAME")
    private List<String> repositoryName = new ArrayList<>();

    @Option(names = "-u", description = "Get all repositories with matching URL", paramLabel = "REPOSITORY_URL")
    private List<URL> repositoryUrl = new ArrayList<>();

    @Override
    public void run() {

        RepositoryController.getRepositories().stream()
                .filter(repository ->
                        (repositoryName.isEmpty() || repositoryName.contains(repository.getName()))
                     || (repositoryUrl.isEmpty() || repositoryUrl.contains(repository.getUrl())))
                .forEach(System.out::println);
    }
}