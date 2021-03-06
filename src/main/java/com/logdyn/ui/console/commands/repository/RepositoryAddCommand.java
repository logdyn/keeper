package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.net.URL;
import java.util.Optional;

@Component
@Command(name = "add", description = "Add a remote repository")
public class RepositoryAddCommand extends CliCommand {

    @Parameters(index = "0", description = "The URL of the repository to add", paramLabel = "REPOSITORY_URL")
    private URL repositoryUrl;

    @Parameters(index = "1", description = "Used to set the name of the repository, otherwise defined by remote",
            arity = "0..1", paramLabel = "REPOSITORY_NAME")
    private String repositoryName;

    private final RepositoryController repositoryController;

    public RepositoryAddCommand(final RepositoryController repositoryController)
    {
        this.repositoryController = repositoryController;
    }

    @Override
    public void run() {
        final Optional<? extends Repository> repo = repositoryController.addRepository(repositoryUrl);
        if (repositoryName != null) {
            repo.ifPresent(repository -> repository.setName(repositoryName));
        }

        if (repo.isPresent()) {
            System.out.println("Added repository: " + repo.get());
        } else {
            System.err.println("Could not add repository from url: " + repositoryUrl);
        }
    }
}
