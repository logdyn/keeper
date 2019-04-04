package com.logdyn.core.task;

public class WorkLog {
    private final Timer timer;
    private String comment;

    public WorkLog() {
        this(System.currentTimeMillis(), 0, "");
    }

    public WorkLog(final long start, final long duration, final String comment) {
        this.comment = comment;
        this.timer = new Timer(start, duration);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
    }

    public Timer getTimer() {
        return timer;
    }
}
