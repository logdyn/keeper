package com.logdyn.core.persistence;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.logdyn.core.persistence.dto.State;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.core.repository.factories.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.FileSystemException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class StorageController {

    private static Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    private final File file;
    private final RepositoryController repositoryController;
    private final Collection<RepositoryFactory> factories;

    public StorageController(@Value("${keeper.save.file}") final File file, //NON-NLS
                             final RepositoryController repositoryController,
                             final Collection<RepositoryFactory> factories)
    {
        this.file = file;
        this.repositoryController = repositoryController;
        this.factories = factories;
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
            xmlMapper.writeValue(this.file, new State(1, this.repositoryController.getRepositories()));
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
            xmlMapper.registerSubtypes(getRepositoryTypes());
            try
            {
                final State state = xmlMapper.readValue(this.file, State.class);
                this.repositoryController.addRepositories(state.getRepositories());
            }
            catch (final IOException e)
            {
                throw new UncheckedIOException(e);
            }

        }
    }

    private NamedType[] getRepositoryTypes()
    {
        final NamedType[] repoTypes = factories.stream()
                .map(Object::getClass)
                .map(Class::getGenericInterfaces)
                .flatMap(Stream::of)
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .map(ParameterizedType::getActualTypeArguments)
                .flatMap(Stream::of)
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .filter(com.logdyn.core.repository.Repository.class::isAssignableFrom)
                .map(NamedType::new)
                .toArray(NamedType[]::new);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("found Repo types: {}", Stream.of(repoTypes)
                    .map(NamedType::getType)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "[", "]")));
        }
        return repoTypes;
    }
}
