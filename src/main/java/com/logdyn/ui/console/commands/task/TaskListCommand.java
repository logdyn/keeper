package com.logdyn.ui.console.commands.task;

import com.logdyn.core.repository.RepositoryController;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Command(name = "list", description = "Lists tasks")
public class TaskListCommand extends CliCommand {

    @Option(names = "-n", description = "Get tasks from all repositories with matching name", paramLabel = "REPOSITORY_NAME")
    private List<String> repositoryName = new ArrayList<>();

    @Option(names = "-u", description = "Get tasks from all repositories with matching URL", paramLabel = "REPOSITORY_URL")
    private List<URL> repositoryUrl = new ArrayList<>();

    @Option(names = "-i", description = "Get tasks with matching ID", paramLabel = "TASK_ID")
    private List<String> taskId = new ArrayList<>();

    @Override
    public void run() {
        RepositoryController.getRepositories().stream()
                .filter(repository ->
                        (repositoryName.isEmpty() || repositoryName.contains(repository.getName()))
                     || (repositoryUrl.isEmpty() || repositoryUrl.contains(repository.getUrl())))
                .flatMap(repository -> repository.getTasks().stream())
                .filter(task -> taskId.isEmpty() || taskId.contains(task.getId()))
                .forEach(System.out::println);
    }
}
