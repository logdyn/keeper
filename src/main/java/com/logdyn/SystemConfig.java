package com.logdyn;

import picocli.CommandLine.IVersionProvider;

import java.util.Objects;

public final class SystemConfig implements IVersionProvider {

    public static final String VERSION;

    static{
        VERSION = Objects.toString(SystemConfig.class.getPackage().getImplementationVersion(), "Dev"); //NON-NLS
    }

    @Override
    public String[] getVersion() throws Exception {
        return new String[]{VERSION};
    }
}
