package com.logdyn.core.repository.factories;

import com.logdyn.core.authentication.Authenticator;
import com.logdyn.core.repository.JiraRepository;
import com.logdyn.core.repository.Repository;
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
    public Optional<Repository> createRepository(final URL url, final Authenticator auth) {
        try (final InputStream stream = new URL(url, SERVER_INFO_PATH).openStream())
        {
            final JSONObject json = new JSONObject(new JSONTokener(stream));
            String baseUrl = json.getString(BASE_URL_KEY);
            String name = json.getString(SERVER_TITLE_KEY);
            if (name == null) {
                return Optional.empty();
            }

            return Optional.of(new JiraRepository(new URL(baseUrl), name, auth));
        }
        catch (final IOException e) {
            LOGGER.debug(String.format("Could not create JIRA Repository from URL: %s", url), e);
            return Optional.empty();
        }
    }
}