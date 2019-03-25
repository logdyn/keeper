package com.logdyn.core.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.authentication.Authenticator;
import com.logdyn.core.repository.dto.JiraWorkLog;
import com.logdyn.core.task.Task;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class JiraRepository extends Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JiraRepository.class);

    private static final String API_PATH = "/rest/api/2"; //NON-NLS
    private static final String ISSUE_PATH = API_PATH + "/issue/"; //NON-NLS
    private static final String ISSUE_BROWSER_PATH = "/browse/"; //NON-NLS
    private static final String WORKLOG_PATH = "/worklog"; //NON-NLS
    private static final String ISSUE_ID_KEY = "key"; //NON-NLS
    private static final String ISSUE_FIELDS_KEY = "fields"; //NON-NLS
    private static final String ISSUE_TITLE_KEY = "summary"; //NON-NLS
    private static final String ISSUE_DESC_KEY = "description"; //NON-NLS
    private static final String POST_REQUEST_METHOD = "POST"; //NON-NLS
    private static final String CONTENT_TYPE_KEY = "Content-Type"; //NON-NLS
    private static final String JSON_CONTENT_TYPE = "application/json"; //NON-NLS

    public JiraRepository(final URL url, final String name) {
        super(url, name);
    }
    @JsonCreator
    public JiraRepository(final URL url, final String name, final Authenticator auth) {
        super(url, name, auth);
    }

    @Override
    String getTaskId(final URL url){
        final String path = url.getPath();
        final int lastSeparator = path.lastIndexOf('/');
        return path.substring(lastSeparator + 1);
    }

    @Override
    public Optional<Task> getRemoteTask(final String id) throws AuthenticationRequiredException {
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(this.url, ISSUE_PATH + id).openConnection();
            if (this.auth != null) {
                auth.authenticate(conn);
            }
            final int resCode = conn.getResponseCode();
            if (resCode == 200) {
                return createTask(conn);
            }
            else if (resCode == 401)
            {
                //* Authentication
                throw new AuthenticationRequiredException(this, this.auth);
            }
        }
        catch (final IOException ioe){
            LOGGER.debug(String.format("IOException when getting Task '%s' from Jira Repository '%s'", id, this.name), ioe);
            throw new UncheckedIOException(ioe);
        }
        return Optional.empty();
    }

    private Optional<Task> createTask(final HttpURLConnection conn) throws IOException {
        try(final InputStream stream = conn.getInputStream()) {
            final JSONObject json = new JSONObject(new JSONTokener(stream));
            final String key = json.getString(ISSUE_ID_KEY);
            final JSONObject fields = json.getJSONObject(ISSUE_FIELDS_KEY);
            final String title = fields.getString(ISSUE_TITLE_KEY);
            final String desc = fields.optString(ISSUE_DESC_KEY,"");
            return Optional.of(new Task(key, title, desc, new URL(this.url, ISSUE_BROWSER_PATH + key)));

        } catch (final JSONException e) {
            LOGGER.debug(String.format("Exception parsing JSON in createTask() of Jira Repository '%s'", this.name), e);
            return Optional.empty();
        }
    }

    @Override
    public void submitTask(final Task task) throws AuthenticationRequiredException {
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(this.url, ISSUE_PATH + task.getId() + WORKLOG_PATH).openConnection();
            if (this.auth != null) {
                auth.authenticate(conn);
            }
            conn.setRequestMethod(POST_REQUEST_METHOD);
            conn.setDoOutput(true);
            conn.setRequestProperty(CONTENT_TYPE_KEY, JSON_CONTENT_TYPE);
            try (final OutputStream outputStream = conn.getOutputStream()) {
                new ObjectMapper().writeValue(outputStream, new JiraWorkLog(task.getCurrentWorkLog()));
            }
            final int resCode = conn.getResponseCode();
            if (resCode == 401){
                throw new AuthenticationRequiredException(this, this.auth);
            }
            else if(resCode != 201){
                final JSONObject errorJson = new JSONObject(new JSONTokener(conn.getErrorStream()));
                throw new IllegalArgumentException(resCode + ": " + errorJson);
            }
        } catch (final IOException ioe) {
            LOGGER.debug(String.format("IOException when submitting WorkLog to Task '%s' in Jira Repository '%s'", task.getId(), this.name), ioe);
            throw new UncheckedIOException(ioe);
        }
    }
}
