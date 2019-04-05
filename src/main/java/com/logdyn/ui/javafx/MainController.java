package com.logdyn.ui.javafx;

import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.authentication.Authenticator;
import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.core.task.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Component
public class MainController
{
    private static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @FXML
    private TreeTableView<Object> table;

    @FXML
    private TreeTableColumn<Object, String> nameCol;

    @FXML
    private TreeTableColumn<Object, String> timeCol;

    @Autowired
    private RepositoryController repositoryController;

    @FXML
    private void initialize()
    {
        nameCol.setCellValueFactory(param -> {
            final Object value = param.getValue().getValue();
            if (value instanceof Task)
            {
                return new SimpleStringProperty(((Task) value).getTitle());
            }
            else if(value instanceof Repository)
            {
                return new SimpleStringProperty(((Repository) value).getName());
            }
            else
            {
                return null;
            }
        });
        timeCol.setCellValueFactory(param -> {
            final Object value = param.getValue().getValue();
            if (value instanceof Task)
            {
                return new SimpleStringProperty(String.valueOf(((Task) value).getWorkLog().getTimer().getDuration()));
            }
            else
            {
                return null;
            }
        });

        this.table.setShowRoot(false);
        fillTable();
        setEvents();
    }

    private void fillTable()
    {
        final TreeItem<Object> root = new TreeItem<>("repos");
        for (final Repository repository : repositoryController.getRepositories())
        {
            final TreeItem<Object> repositoryTreeItem = new TreeItem<>(repository);
            repositoryTreeItem.setExpanded(true);
            for (final Task task : repository.getTasks())
            {
                repositoryTreeItem.getChildren().add(new TreeItem<>(task));
            }
            root.getChildren().add(repositoryTreeItem);
        }
        this.table.setRoot(root);
    }

    private void setEvents()
    {
        this.table.setOnDragOver(e -> {
            if (e.getDragboard().hasString())
            {
                e.acceptTransferModes(TransferMode.ANY);
            }
            e.consume();
        });
        this.table.setOnDragDropped(e -> {
            if (e.getDragboard().hasUrl())
            {
                try
                {
                    final URL url = new URL(e.getDragboard().getUrl());
                    this.repositoryController.getRepository(url)
                            .ifPresent(r -> fillTable());
                    e.setDropCompleted(true);
                }
                catch (final MalformedURLException ex)
                {
                    throw new UncheckedIOException(ex);
                }
            }
            e.consume();
        });

        this.table.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.F5))
            {
                fillTable();
            }
            else if (event.isControlDown() && event.getCode().equals(KeyCode.N))
            {
                final TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setContentText("enter url");
                textInputDialog.showAndWait().ifPresent(s -> {
                    final URL url;
                    try
                    {
                        url = new URL(s);
                    }
                    catch (final MalformedURLException e)
                    {
                        throw new UncheckedIOException(e);
                    }
                    Optional<Task> task = Optional.empty();
                    while (!task.isPresent())
                    {
                        try
                        {
                            task = this.repositoryController.getTask(url);
                        }
                        catch (final AuthenticationRequiredException e)
                        {
                            final Optional<? extends Authenticator> authenticator = new BasicAuthDialog().showAndWait();
                            if (authenticator.isPresent())
                            {
                                this.repositoryController.getRepository(url).ifPresent(
                                        r -> r.setAuthenticator(authenticator.get()));
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                    task.ifPresent(t -> fillTable());
                });
            }
            event.consume();
        });
    }
}
