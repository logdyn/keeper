package com.logdyn.core.task;

import java.net.URL;

public class Task {
    private final String id;
    private String title;
    private String description;
    private final URL url;
    private WorkLog workLog;

    public Task(final String id, final String title, final String description, final URL url) {
        this(id, title, description, url, new WorkLog());
    }

    public Task(final String id, final String title, final String description, final URL url, final WorkLog workLog)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.workLog = workLog;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() { return this.title; }
    public void setTitle(final String title) { this.title = title; }

    public String getDescription() { return this.description; }
    public void setDescription(final String description) { this.description = description; }

    public URL getURL() { return this.url; }

    public WorkLog getCurrentWorkLog() { return this.workLog; }
    public void setCurrentWorkLog(final WorkLog workLog) { this.workLog = workLog; }
}
