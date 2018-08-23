package com.logdyn.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

class Timer {
    private LongProperty init;
    private long start;
    private long duration;

    Timer(final long start) {
        this(start, 0);
    }

    Timer(final long start, final long duration) {
        this.init = new SimpleLongProperty(start);
        this.start = -1;
        this.duration = duration;
    }

    public LongProperty initProperty() {
        return init;
    }

    public long getDuration() {
        return this.duration + this.getSinceStarted();
    }

    public boolean isStopped() {
        return this.start == -1;
    }

    public long start() {
        this.duration += this.getSinceStarted();
        this.start = System.currentTimeMillis();
        if (this.init.get() == -1) {
            this.init.set(this.start);
        }
        return this.start;
    }

    public long stop() {
        this.duration += this.getSinceStarted();
        this.start = -1;
        return this.duration;
    }

    public long addMillis(final long millis) {
        this.start -= millis;
        return getDuration();
    }

    private long getSinceStarted() {
        return isStopped() ? 0 : (System.currentTimeMillis() - this.start);
    }
}
