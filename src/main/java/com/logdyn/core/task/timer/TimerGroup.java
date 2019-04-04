package com.logdyn.core.task.timer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    public Optional<Long> stop()
    {
        return this.timers.stream()
                .map(Timer::stop)
                .flatMap(Optional::stream)
                .findAny();
    }

    @Override
    public long getDuration()
    {
        return this.timers.stream()
                .map(Timer::getDuration)
                .reduce(Long::sum)
                .orElse(0L);
    }

    @Override
    public Optional<Long> getStartTime()
    {
        return this.timers.stream()
                .map(Timer::getStartTime)
                .flatMap(Optional::stream)
                .reduce(Long::min);
    }

    @Override
    public Optional<Long> getEndTime()
    {
        return this.timers.stream()
                .map(Timer::getEndTime)
                .flatMap(Optional::stream)
                .reduce(Long::max);
    }

    @Override
    public List<Long> getTimes()
    {
        return this.timers.stream()
                .map(Timer::getTimes)
                .flatMap(Collection::stream)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }
}
