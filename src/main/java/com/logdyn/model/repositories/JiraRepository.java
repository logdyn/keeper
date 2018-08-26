package com.logdyn.model.repositories;

import com.logdyn.model.Task;
import com.logdyn.model.auth.AuthenticationRequiredException;
import com.logdyn.model.auth.Authenticator;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class JiraRepository implements Repository {
    private static final String API_PATH = "/rest/api/2";
    private static final String ISSUE_PATH = API_PATH + "/issue/";
    private static final String ISSUE_ID_KEY = "key";
    private static final String ISSUE_FIELDS_KEY = "fields";
    private static final String ISSUE_TITLE_KEY = "summary";
    private static final String ISSUE_DESC_KEY = "description";

    private String name;
    private URL url;
    private Optional<Authenticator> auth;

    public JiraRepository(final URL url, final String name) {
        this(url, name, null);
    }

    public JiraRepository(final URL url, final String name, final Authenticator auth) {
        this.url = url;
        this.name = name;
        this.auth = Optional.ofNullable(auth);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public void setAuthenticator(final Authenticator auth) {
        this.auth = Optional.ofNullable(auth);
    }

    @Override
    public boolean isUrlMatch(final URL url) {
        return this.url.getHost().equals(url.getHost());
    }

    @Override
    public Optional<Task> getTask(final URL url) throws AuthenticationRequiredException {
        if (!isUrlMatch(url)){
            return Optional.empty();
        }
        return this.getTask(this.getKey(url));
    }

    private String getKey(final URL url){
        final String path = url.getPath();
        final int lastSeparator = path.lastIndexOf('/');
        return path.substring(lastSeparator + 1);
    }

    @Override
    public Optional<Task> getTask(final String id) throws AuthenticationRequiredException {
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(this.url, ISSUE_PATH + id).openConnection();
            this.auth.ifPresent(auth -> auth.authenticate(conn));
            final int resCode = conn.getResponseCode();
            if (resCode == 200) {
                try (final InputStream stream = conn.getInputStream()) {
                    return createTask(stream);
                }
            }
            else if (resCode == 401)
            {
                //* Authentication
                throw new AuthenticationRequiredException(this, this.auth.orElse(null));
            }
        }
        catch (final IOException ioe){
            throw new UncheckedIOException(ioe);
        }
        return Optional.empty();
    }

    private Optional<Task> createTask(final InputStream stream){
        try {
            final JSONObject json = new JSONObject(new JSONTokener(stream));
            final String key = json.getString(ISSUE_ID_KEY);
            final JSONObject fields = json.getJSONObject(ISSUE_FIELDS_KEY);
            final String title = fields.getString(ISSUE_TITLE_KEY);
            final String desc = fields.optString(ISSUE_DESC_KEY,"");
            return Optional.of(new Task(key, title, desc));

        } catch (final JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }
}
