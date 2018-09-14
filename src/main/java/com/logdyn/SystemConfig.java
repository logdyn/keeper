package com.logdyn;

import java.util.Objects;

public final class SystemConfig {

    public static final String VERSION;

    static{
        VERSION = Objects.toString(SystemConfig.class.getPackage().getImplementationVersion(), "Dev"); //NON-NLS
    }

    private SystemConfig() {
        throw new AssertionError("Cannot instantiate a utility class");
    }
}
