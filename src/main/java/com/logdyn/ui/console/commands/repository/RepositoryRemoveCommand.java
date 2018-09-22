package com.logdyn.ui.console.commands.repository;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void run() {

        final Collection<Repository> removalList;
        if (all) {
            removalList = new ArrayList<>(RepositoryController.getRepositories());
        }
        else{
            removalList = new ArrayList<>(repositoryName.size() + repositoryUrl.size());
            repositoryName.stream()
                    .map(RepositoryController::getRepository)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(removalList::add);
            repositoryUrl.stream()
                    .map(RepositoryController::getRepository)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(removalList::add);
        }

        if (!force) {
            //* Prompt user for confirmation of removal
            final Reader reader = new InputStreamReader(System.in);
            removalList.removeIf(repository -> {
                System.out.printf("Remove repository: %s? [Y|n]%n", repository);
                try {
                    final char result = (char) reader.read();
                    return result != 'y' && result != 'Y';
                } catch (final IOException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }

        removalList.forEach(RepositoryController::removeRepository);

        System.out.printf("Removed %d repositories%n", removalList.size());
    }
}
