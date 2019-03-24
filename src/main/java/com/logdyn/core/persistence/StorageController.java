package com.logdyn.core.persistence;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.logdyn.core.persistence.dto.State;
import com.logdyn.core.repository.JiraRepository;
import com.logdyn.core.repository.RepositoryController;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystemException;

public final class StorageController {
    private static final String FILEPATH = System.getProperty("user.home") + "/.keeper/state.xml"; //NON-NLS

    private StorageController() {
        throw new AssertionError();
    }

    public static void save()
    {
        try
        {
            final File file = new File(FILEPATH);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdir()) {
                throw new FileSystemException(file.getParent(), null, "could not create folder");
            }

            final XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
            xmlMapper.writeValue(file, new State(1, RepositoryController.getRepositories()));
        }
        catch (final IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public static void load()
    {
        final File file = new File(FILEPATH);
        if (file.exists()){
            final XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new ParameterNamesModule());
            xmlMapper.registerSubtypes(JiraRepository.class);
            try
            {
                final State state = xmlMapper.readValue(file, State.class);
                RepositoryController.addRepositories(state.getRepositories());
            }
            catch (final IOException e)
            {
                throw new UncheckedIOException(e);
            }

        }
    }
}
