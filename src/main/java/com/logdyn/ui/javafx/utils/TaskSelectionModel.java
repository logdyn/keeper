package com.logdyn.ui.javafx.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;

public class TaskSelectionModel extends TreeTableView.TreeTableViewSelectionModel<Object>
{
    private final ObservableList<TreeTablePosition<Object, ?>> cells = FXCollections.observableArrayList();

    public TaskSelectionModel(final TreeTableView<Object> treeTableView)
    {
        super(treeTableView);
    }

    @Override
    public ObservableList<TreeTablePosition<Object, ?>> getSelectedCells()
    {
        return FXCollections.unmodifiableObservableList(this.cells);
    }

    @Override
    public boolean isSelected(final int row, final TableColumnBase<TreeItem<Object>, ?> column)
    {
        return this.isSelected(row);
    }

    @Override
    public void select(final int row, final TableColumnBase<TreeItem<Object>, ?> column)
    {
        this.select(row);
    }

    @Override
    public void clearSelection()
    {
        cells.clear();
    }

    @Override
    public void clearSelection(final int index)
    {
        super.clearSelection(index);
    }

    @Override
    public boolean isSelected(final int index)
    {
        return cells.stream()
                .map(TablePositionBase::getRow)
                .anyMatch(Integer.valueOf(index)::equals);
    }

    @Override
    public boolean isEmpty()
    {
        return super.isEmpty();
    }

    @Override
    public void select(final int row)
    {
        final TreeTableView<Object> treeTableView = this.getTreeTableView();
        cells.add(new TreeTablePosition<>(treeTableView, row, null));
        this.getTreeTableView().getFocusModel().focus(row);
        this.getTreeTableView().refresh();
    }

    @Override
    public void clearAndSelect(final int row)
    {
        this.clearSelection();
        this.select(row);
    }

    @Override
    public void clearAndSelect(final int row, final TableColumnBase<TreeItem<Object>, ?> column)
    {
        this.clearAndSelect(row);
    }

    @Override
    public void clearSelection(final int row, final TableColumnBase<TreeItem<Object>, ?> column)
    {
        if(isSelected(row, column))
        {
            this.clearSelection();
        }
    }

    @Override
    public void selectLeftCell()
    {

    }

    @Override
    public void selectRightCell()
    {

    }

    @Override
    public void selectAboveCell()
    {

    }

    @Override
    public void selectBelowCell()
    {

    }
}
