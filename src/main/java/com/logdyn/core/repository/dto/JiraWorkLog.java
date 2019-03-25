package com.logdyn.core.repository.dto;

import com.logdyn.core.task.WorkLog;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JiraWorkLog
{
    private static final DateTimeFormatter JIRA_TIME_FORMAT = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSxxxx"); //NON-NLS

    private final WorkLog workLog;

    public JiraWorkLog(final WorkLog workLog)
    {
        this.workLog = workLog;
    }

    public String getStarted()
    {
        final Instant instant = Instant.ofEpochMilli(workLog.getTimer().getStartTime());
        final ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        return JIRA_TIME_FORMAT.format(dateTime);
    }

    public long getTimeSpentSeconds()
    {
        return this.workLog.getTimer().getDuration() / 1000;
    }

    public String getComment()
    {
        return this.workLog.getComment();
    }
}
