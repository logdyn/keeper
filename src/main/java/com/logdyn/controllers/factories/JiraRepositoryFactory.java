package com.logdyn.controllers.factories;

import com.logdyn.model.repositories.JiraRepository;
import com.logdyn.model.repositories.Repository;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

public class JiraRepositoryFactory implements RepositoryFactory {
    private static final Logger LOGGER = Logger.getLogger(JiraRepositoryFactory.class);
    private static final String SERVER_INFO_PATH = "/rest/api/2/serverInfo"; //NON-NLS
    private static final String BASE_URL_KEY = "baseUrl"; //NON-NLS
    private static final String SERVER_TITLE_KEY = "serverTitle"; //NON-NLS

    @Override
    public Optional<Repository> createRepository(final URL url) {
        return this.createRepository(url, null);
    }

    @Override
    public Optional<Repository> createRepository(final URL url, final String name) {
        try (final InputStream stream = new URL(url, SERVER_INFO_PATH).openStream())
        {
            final JSONObject json = new JSONObject(new JSONTokener(stream));
            final String baseUrl = json.getString(BASE_URL_KEY);
            final String serverName = json.getString(SERVER_TITLE_KEY);
            if (serverName == null) {
                return Optional.empty();
            }

            return Optional.of(new JiraRepository(new URL(baseUrl), name != null ? name : serverName));
        }
        catch (final IOException e) {
            LOGGER.debug(String.format("Could not create JIRA Repository from URL: %s", url), e);
            return Optional.empty();
        }
    }
}
