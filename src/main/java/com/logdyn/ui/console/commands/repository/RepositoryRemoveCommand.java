package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.ConsoleUtils;
import com.logdyn.ui.console.commands.CliCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Command(name = "remove", description = "Removes any repositories matching the given names or URLs")
public class RepositoryRemoveCommand extends CliCommand {

    @Option(names = "-n", description = "Removes all repositories with matching name", paramLabel = "REPOSITORY_NAME")
    private List<String> repositoryName = new ArrayList<>();

    @Option(names = "-u", description = "Remove all repositories with matching URL", paramLabel = "REPOSITORY_URL")
    private List<URL> repositoryUrl = new ArrayList<>();

    @Option(names = {"-a", "--all"}, description = "Removes all repositories")
    private boolean all;

    @Option(names = {"-f", "--force"}, description = "Does not prompt before removal of repository")
    private boolean force;

    private final RepositoryController repositoryController;

    public RepositoryRemoveCommand(final RepositoryController repositoryController)
    {
        this.repositoryController = repositoryController;
    }

    @Override
    public void run() {

        final Collection<Repository> removalList;
        if (all) {
            removalList = new ArrayList<>(this.repositoryController.getRepositories());
        }
        else{
            removalList = new ArrayList<>(repositoryName.size() + repositoryUrl.size());
            repositoryName.stream()
                    .map(repositoryController::getRepository)
                    .flatMap(Optional::stream)
                    .forEach(removalList::add);
            repositoryUrl.stream()
                    .map(repositoryController::getRepository)
                    .flatMap(Optional::stream)
                    .forEach(removalList::add);
        }

        if (!force) {
            //* Prompt user for confirmation of removal
            removalList.removeIf(repository -> !ConsoleUtils.promptBoolean("Remove repository: %s?", repository));
        }

        removalList.forEach(repositoryController::removeRepository);

        System.out.printf("Removed %d repositories%n", removalList.size());
    }
}
