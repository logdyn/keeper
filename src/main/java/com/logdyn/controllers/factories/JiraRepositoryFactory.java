package com.logdyn.controllers.factories;

import com.logdyn.model.auth.Authenticator;
import com.logdyn.model.repositories.JiraRepository;
import com.logdyn.model.repositories.Repository;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

public class JiraRepositoryFactory implements RepositoryFactory {
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
            return Optional.empty();
        }
    }
}
