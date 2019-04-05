package com.logdyn.core.task.timer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;

public class SingleTimer implements Timer
{
    private final List<Long> times;
    private final LongSupplier timeSupplier;

    public SingleTimer()
    {
        this.times = new ArrayList<>();
        this.timeSupplier = System::currentTimeMillis;
    }

    @JsonCreator
    public SingleTimer(final Collection<Long> times)
    {
        this(times, System::currentTimeMillis);
    }

    public SingleTimer(final Collection<Long> times, final LongSupplier timeSupplier)
    {
        this.times = times == null ? new ArrayList<>() : new ArrayList<>(times);
        this.timeSupplier = timeSupplier;
    }

    @Override
    public boolean isRunning()
    {
        return (times.size() & 1) == 1;
    }

    @Override
    public boolean hasStarted()
    {
        return !times.isEmpty();
    }

    public Optional<Long> start()
    {
        return this.toggle(true);
    }

    @Override
    public Optional<Long> stop()
    {
        return this.toggle(false);
    }

    public Optional<Long> toggle(final boolean start)
    {
        if (this.isRunning() != start)
        {
            return Optional.of(this.toggle());
        }
        return Optional.empty();
    }

    public long toggle()
    {
        final long time = this.timeSupplier.getAsLong();
        this.times.add(time);
        return time;
    }

    @Override
    public long getDuration()
    {
        long result = 0;
        for (final Iterator<Long> i = this.times.iterator(); i.hasNext();)
        {
            final long start = i.next();
            final long end = i.hasNext() ? i.next() : this.timeSupplier.getAsLong();
            result += (end - start);
        }
        return result;
    }

    @Override
    public Optional<Long> getStartTime()
    {
        final Iterator<Long> i = this.times.iterator();
        return i.hasNext() ? Optional.of(i.next()) : Optional.empty();
    }

    @Override
    public Optional<Long> getEndTime()
    {
        final int size = this.times.size();
        if (size == 0)
        {
            return Optional.empty();
        }
        else if (this.isRunning())
        {
            return Optional.of(this.timeSupplier.getAsLong());
        }
        else
        {
            return Optional.of(this.times.get(size - 1));
        }

    }

    @Override
    public List<Long> getTimes()
    {
        return Collections.unmodifiableList(this.times);
    }
}
