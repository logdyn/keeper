package com.logdyn;

public final class SystemConfig {

    public static final String VERSION;

    static{
        VERSION = SystemConfig.class.getPackage().getImplementationVersion();
    }

    private SystemConfig() {
        throw new AssertionError("Cannot instantiate a utility class");
    }
}
