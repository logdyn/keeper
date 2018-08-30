package com.logdyn.controllers.persistence;

import com.logdyn.controllers.persistence.adapters.Adapter;
import com.logdyn.controllers.persistence.adapters.NullAdapter;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collection;

public final class StorageController {
    private static Collection<Adapter> adapters = new ArrayList<>();

    static {
        adapters.add(new NullAdapter(adapters.size()));
    }

    private StorageController() {
        throw new AssertionError();
    }

    public static void save() {

    }

    public static void open() {

    }

    private static Document adapt(final Document document) {
        return adapters.stream()
                .sorted()
                .sequential()
                .reduce(document, (doc, adapter) -> adapter.adapt(doc), null);
    }
}
