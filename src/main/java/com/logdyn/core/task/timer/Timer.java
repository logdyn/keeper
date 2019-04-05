package com.logdyn.core.task.timer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Optional;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({@JsonSubTypes.Type(SingleTimer.class), @JsonSubTypes.Type(TimerGroup.class)})
public interface Timer
{
    @JsonIgnore
    boolean isRunning();

    @JsonIgnore
    boolean hasStarted();

    Optional<Long> stop();

    @JsonIgnore
    long getDuration();

    @JsonIgnore
    Optional<Long> getStartTime();

    @JsonIgnore
    Optional<Long> getEndTime();

    List<Long> getTimes();
}
