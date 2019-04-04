package com.logdyn.core.task;

import com.logdyn.core.task.timer.SingleTimer;
import com.logdyn.core.task.timer.Timer;

public class WorkLog {
    private final Timer timer;
    private String comment;

    public WorkLog()
    {
        this(new SingleTimer(), "");
    }

    public WorkLog(final Timer timer, final String comment)
    {
        this.comment = comment;
        this.timer = timer;
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
