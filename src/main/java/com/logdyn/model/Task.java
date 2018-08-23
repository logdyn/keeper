package com.logdyn.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private final String id;
    private final StringProperty title;
    private final StringProperty description;
    private final ObjectProperty<WorkLog> workLog;

    public Task(final String id, final String title, final String description, final WorkLog workLog)
    {
        this.id = id;
        this.title = new SimpleStringProperty(this, "title", title); //NON-NLS
        this.description = new SimpleStringProperty(this, "description", description); //NON-NLS
        this.workLog = new SimpleObjectProperty<>(this, "workLog", workLog); //NON-NLS
    }

    public StringProperty titleProperty() { return this.title; }
    public String getTitle() { return this.title.get(); }
    public void setTitle(final String title) { this.title.set(title); }

    public StringProperty descriptionProperty() { return this.description; }
    public String getDescription() { return this.description.get(); }
    public void setDescription(final String description) { this.description.set(description); }

    public ObjectProperty<WorkLog> workLogProperty() { return this.workLog; }
    public WorkLog getCurrentWorkLog() { return this.workLog.get(); }
    public void setCurrentWorkLog(final WorkLog workLog) { this.workLog.set(workLog); }
}
