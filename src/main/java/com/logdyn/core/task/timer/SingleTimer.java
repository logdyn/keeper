package com.logdyn.core.task.timer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalLong;
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

    public SingleTimer(final Collection<Long> times)
    {
        this(times, System::currentTimeMillis);
    }

    public SingleTimer(final Collection<Long> times, final LongSupplier timeSupplier)
    {
        this.times = new ArrayList<>(times);
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

    public OptionalLong start()
    {
        return this.toggle(true);
    }

    @Override
    public OptionalLong stop()
    {
        return this.toggle(false);
    }

    public OptionalLong toggle(final boolean start)
    {
        if (this.isRunning() != start)
        {
            return OptionalLong.of(this.toggle());
        }
        return OptionalLong.empty();
    }

    public long toggle()
    {
        final long time = this.timeSupplier.getAsLong();
        this.times.add(time);
        return time;
    }

    @Override
    public long duration()
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
    public OptionalLong startTime()
    {
        final Iterator<Long> i = this.times.iterator();
        return i.hasNext() ? OptionalLong.of(i.next()) : OptionalLong.empty();
    }

    @Override
    public OptionalLong endTime()
    {
        final int size = this.times.size();
        if (size == 0)
        {
            return OptionalLong.empty();
        }
        else if (this.isRunning())
        {
            return OptionalLong.of(this.timeSupplier.getAsLong());
        }
        else
        {
            return OptionalLong.of(this.times.get(size - 1));
        }

    }

    @Override
    public List<Long> getTimes()
    {
        return Collections.unmodifiableList(this.times);
    }
}
