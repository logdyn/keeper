package com.logdyn.core.persistence;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.logdyn.core.persistence.dto.State;
import com.logdyn.core.repository.JiraRepository;
import com.logdyn.core.repository.RepositoryController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystemException;

@Repository
public final class StorageController {

    private File file;

    public StorageController(@Value("${keeper.save.file}") final File file) //NON-NLS
    {
        this.file = file;
    }

    public void save()
    {
        try
        {
            if (!this.file.getParentFile().exists() && !this.file.getParentFile().mkdir()) {
                throw new FileSystemException(this.file.getParent(), null, "could not create folder");
            }

            final XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
            xmlMapper.writeValue(this.file, new State(1, RepositoryController.getRepositories()));
        }
        catch (final IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public void load()
    {
        if (this.file.exists()){
            final XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new ParameterNamesModule());
            xmlMapper.registerSubtypes(JiraRepository.class);
            try
            {
                final State state = xmlMapper.readValue(this.file, State.class);
                RepositoryController.addRepositories(state.getRepositories());
            }
            catch (final IOException e)
            {
                throw new UncheckedIOException(e);
            }

        }
    }
}
