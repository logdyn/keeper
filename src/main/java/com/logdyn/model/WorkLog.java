package com.logdyn.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WorkLog {
    private StringProperty comment;
    private Timer timer;

    public WorkLog() {
        this(System.currentTimeMillis(), 0, "");
    }

    public WorkLog(final long start, final long duration, final String comment) {
        this.comment = new SimpleStringProperty(this, "comment", comment); //NON-NLS
        this.timer = new Timer(start);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public Timer getTimer() {
        return timer;
    }
}
