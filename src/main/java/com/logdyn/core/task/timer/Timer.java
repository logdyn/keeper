package com.logdyn.core.task.timer;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface Timer
{
    boolean isRunning();

    boolean hasStarted();

    OptionalLong stop();

    long duration();

    OptionalLong startTime();

    OptionalLong endTime();

    List<Long> getTimes();
}
