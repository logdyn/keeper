package com.logdyn.core.task.timer;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface Timer
{
    boolean isRunning();

    boolean hasStarted();

    Optional<Long> stop();

    long getDuration();

    Optional<Long> getStartTime();

    Optional<Long> getEndTime();

    List<Long> getTimes();
}
