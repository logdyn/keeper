package com.logdyn.core.task.timer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.OptionalLong;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class SingleTimerTest
{
    @Test
    void isRunning()
    {
        final SingleTimer timer = new SingleTimer();
        assertFalse(timer.isRunning());
        timer.start();
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());
    }

    @Test
    void hasStarted()
    {
        final SingleTimer timer = new SingleTimer();
        assertFalse(timer.hasStarted());
        timer.start();
        assertTrue(timer.hasStarted());
        timer.stop();
        assertTrue(timer.hasStarted());
    }

    @Test
    void start()
    {
        final AtomicLong fakeTime = new AtomicLong(new Random().nextLong());
        final SingleTimer timer = new SingleTimer(List.of(), fakeTime::incrementAndGet);

        OptionalLong startVal = timer.start();
        assertTrue(startVal.isPresent());
        assertEquals(fakeTime.get(), startVal.getAsLong());

        startVal = timer.start();
        assertFalse(startVal.isPresent());
    }

    @Test
    void stop()
    {
        final AtomicLong fakeTime = new AtomicLong(new Random().nextLong());
        final SingleTimer timer = new SingleTimer(List.of(), fakeTime::incrementAndGet);

        OptionalLong stopVal = timer.stop();
        assertFalse(stopVal.isPresent());

        timer.start();
        stopVal = timer.stop();
        assertTrue(stopVal.isPresent());
        assertEquals(fakeTime.get(), stopVal.getAsLong());

        stopVal = timer.stop();
        assertFalse(stopVal.isPresent());
    }

    @Test
    void toggle()
    {

    }

    @Test
    void toggle1()
    {
    }

    @Test
    void duration()
    {
        final AtomicLong fakeTime = new AtomicLong(new Random().nextLong());
        final SingleTimer timer = new SingleTimer(List.of(), fakeTime::incrementAndGet);

        final int duration = new Random().nextInt(100_000);
        for (int i = 0; i < duration; i++)
        {
            timer.start();
            timer.stop();
        }
        assertEquals(duration, timer.duration());
    }

    @Test
    void startTime()
    {
    }

    @Test
    void endTime()
    {
    }

    @Test
    void getTimes()
    {
    }
}
