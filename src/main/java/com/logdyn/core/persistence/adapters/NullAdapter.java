package com.logdyn.core.persistence.adapters;

import org.w3c.dom.Document;

public class NullAdapter extends Adapter {

    public NullAdapter(final int version) {
        super(version);
    }

    @Override
    public int getVersion(final Document document) {
        return this.getVersion();
    }

    @Override
    public Document doAdapt(final Document document) {
        return document;
    }
}
