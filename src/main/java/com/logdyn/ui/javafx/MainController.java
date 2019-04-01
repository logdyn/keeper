package com.logdyn.ui.javafx;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableView;
import org.springframework.stereotype.Component;

@Component
public class MainController
{
    @FXML
    private TreeTableView<?> table;

    @FXML
    private void initialize()
    {
        this.table.getColumns().stream()
                .map(TableColumnBase::visibleProperty)
                .forEach(p -> p.addListener(o -> updateWidthBindings()));
        updateWidthBindings();
    }

    private void updateWidthBindings()
    {
        final long size = this.table.getColumns().stream()
                .filter(TableColumnBase::isVisible)
                .count();
        final DoubleBinding eachWidth = this.table.widthProperty().divide(size);
        this.table.getColumns().stream()
                .map(TableColumnBase::prefWidthProperty)
                .forEach(w -> w.bind(eachWidth));
    }
}
