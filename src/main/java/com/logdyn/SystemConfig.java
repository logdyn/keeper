package com.logdyn;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import picocli.CommandLine.IVersionProvider;

import java.util.Optional;

@Component
public final class SystemConfig implements IVersionProvider {


    private String[] version;

    public SystemConfig(final Optional<BuildProperties> buildProperties)
    {
        this.version = new String[]{buildProperties
                        .map(BuildProperties::getVersion)
                        .orElse("dev")};
    }

    @Override
    public String[] getVersion() {
        return version;
    }
}
