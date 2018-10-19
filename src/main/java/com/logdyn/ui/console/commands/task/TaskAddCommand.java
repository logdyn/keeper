package com.logdyn.ui.console.commands.task;

import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.core.task.Task;
import com.logdyn.ui.console.ConsoleUtils;
import com.logdyn.ui.console.commands.CliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.net.URL;
import java.util.Optional;

@Command(name = "add", description = "Add a task")
public class TaskAddCommand extends CliCommand {

    @Parameters(index = "0", description = "The URL of the task to add", paramLabel = "TASK_URL")
    private URL taskUrl;

    @Parameters(index = "1", description = "Used to set the title of the task, otherwise defined by remote",
            arity = "0..1", paramLabel = "TASK_TITLE")
    private String taskTitle;

    @Override
    public void run() {
        Optional<Task> task = Optional.empty();

        try {
            task = RepositoryController.getTask(taskUrl);
            if (taskTitle != null) {
                task.ifPresent(t -> t.setTitle(taskTitle));
            }
        } catch (AuthenticationRequiredException e) {
            //! TODO Handle authentication request
        } finally {
            if (task.isPresent()) {
                System.out.println("Added task: " + task.get());
            } else {
                System.err.println("Could not add task from url: " + taskUrl);
            }
        }
    }
}
