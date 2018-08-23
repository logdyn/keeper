package com.logdyn.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WorkLog {
    private LongProperty duration;
    private StringProperty comment;
    private Timer tracker;

    public WorkLog() {
        this(System.currentTimeMillis(), 0, "");
    }

    public WorkLog(final long start, final long duration, final String comment) {
        this.duration = new SimpleLongProperty(this, "duration", duration);
        this.comment = new SimpleStringProperty(this, "comment", comment);
        this.tracker = new Timer(start);
    }

    public long getStart() {
        return tracker.getStart();
    }

    public long getDuration() {
        return duration.get();
    }

    public String getComment() {
        return comment.get();
    }

    public LongProperty startProperty() {
        return tracker.startProperty();
    }

    public LongProperty durationProperty() {
        return duration;
    }

    public StringProperty commentProperty() {
        return comment;
    }
}
