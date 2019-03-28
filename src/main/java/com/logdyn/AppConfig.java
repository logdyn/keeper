package com.logdyn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine.HelpCommand;

@Configuration
public class AppConfig
{
    @Bean
    public HelpCommand helpCommand()
    {
        return new HelpCommand();
    }
}
