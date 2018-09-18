package com.logdyn.core.persistence.adapters;

import org.w3c.dom.Document;

public abstract class Adapter implements Comparable<Adapter>{
    private final int version;

    Adapter(final int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public abstract int getVersion(Document document);
    public Document adapt(final Document document) {
        if (getVersion(document) <= this.version) {
            return doAdapt(document);
        }

        return document;
    }

    abstract Document doAdapt(Document document);

    @Override
    public int compareTo(final Adapter other) {
        return Integer.compare(this.version, other.version);
    }
}
