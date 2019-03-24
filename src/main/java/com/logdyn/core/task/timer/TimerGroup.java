package com.logdyn.core.task.timer;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

public class TimerGroup implements Timer
{
    private List<Timer> timers;
    private LongSupplier timeSupplier;

    public TimerGroup(final List<Timer> timers)
    {
        this(timers, System::currentTimeMillis);
    }

    public TimerGroup(final List<Timer> timers, final LongSupplier timeSupplier)
    {
        this.timers = timers;
        this.timeSupplier = timeSupplier;
    }

    public List<Timer> getTimers()
    {
        return this.timers;
    }

    @Override
    public boolean isRunning()
    {
        return this.timers.stream().anyMatch(Timer::isRunning);
    }

    @Override
    public boolean hasStarted()
    {
        return this.timers.stream().anyMatch(Timer::hasStarted);
    }

    @Override
    public OptionalLong stop()
    {
        return this.timers.stream()
                .map(Timer::stop)
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .findAny();
    }

    @Override
    public long duration()
    {
        return this.timers.stream()
                .mapToLong(Timer::duration)
                .sum();
    }

    @Override
    public OptionalLong startTime()
    {
        return this.timers.stream()
                .map(Timer::startTime)
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .min();
    }

    @Override
    public OptionalLong endTime()
    {
        return this.timers.stream()
                .map(Timer::endTime)
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .max();
    }

    @Override
    public List<Long> getTimes()
    {
        return this.timers.stream()
                .flatMap(t -> t.getTimes().stream())
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }
}
