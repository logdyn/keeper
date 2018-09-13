package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.net.URL;
import java.util.Optional;

@Command(name = "add", description = "add a remote repository")
public class RepositoryAddCommand extends CliCommand {

    @Parameters(index = "0", description = "The URL of the repository to add", paramLabel = "REPOSITORY_URL")
    private URL repositoryUrl;

    @Parameters(index = "1", description = "Used to set the name of the repository, otherwise defined by remote",
            arity = "0..1", paramLabel = "REPOSITORY_NAME")
    private String repositoryName;

    @Override
    public void run() {
        final Optional<Repository> repo = RepositoryController.addRepository(repositoryUrl);
        if (repositoryName != null) {
            repo.ifPresent(repository -> repository.setName(repositoryName));
        }

        if (repo.isPresent()) {
            System.out.printf("Added repository %s from %s%n", repo.get().getName(), repo.get().getUrl());
        } else {
            System.err.printf("Could not add repository from %s%n", repositoryUrl);
        }
    }
}
