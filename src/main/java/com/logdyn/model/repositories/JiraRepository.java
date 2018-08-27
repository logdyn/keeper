package com.logdyn.model.repositories;

import com.logdyn.model.Task;
import com.logdyn.model.WorkLog;
import com.logdyn.model.auth.AuthenticationRequiredException;
import com.logdyn.model.auth.Authenticator;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class JiraRepository implements Repository {
    private static final String API_PATH = "/rest/api/2";
    private static final String ISSUE_PATH = API_PATH + "/issue/";
    public static final String WORKLOG_PATH = "/worklog";
    private static final String ISSUE_ID_KEY = "key";
    private static final String ISSUE_FIELDS_KEY = "fields";
    private static final String ISSUE_TITLE_KEY = "summary";
    private static final String ISSUE_DESC_KEY = "description";
    public static final String POST_REQUEST_METHOD = "POST";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String JIRA_TIME_PATTERN = "yyy-MM-dd'T'HH:mm:ss.SSSxxxx";
    public static final String WORKLOG_START_TIME_KEY = "started";
    public static final String WORKLOG_DURATION_KEY = "timeSpentSeconds";
    public static final String WORKLOG_DESC_KEY = "comment";

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
                return createTask(conn);
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

    private Optional<Task> createTask(final HttpURLConnection conn) throws IOException {
        try(final InputStream stream = conn.getInputStream()) {
            final JSONObject json = new JSONObject(new JSONTokener(stream));
            final String key = json.getString(ISSUE_ID_KEY);
            final JSONObject fields = json.getJSONObject(ISSUE_FIELDS_KEY);
            final String title = fields.getString(ISSUE_TITLE_KEY);
            final String desc = fields.optString(ISSUE_DESC_KEY,"");
            return Optional.of(new Task(key, title, desc, conn.getURL()));

        } catch (final JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public void submitTask(final Task task) throws AuthenticationRequiredException {
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(this.url, ISSUE_PATH + task.getId() + WORKLOG_PATH).openConnection();
            this.auth.ifPresent(auth -> auth.authenticate(conn));
            conn.setRequestMethod(POST_REQUEST_METHOD);
            conn.setDoOutput(true);
            conn.setRequestProperty(CONTENT_TYPE_KEY, JSON_CONTENT_TYPE);
            try (final Writer writer = new OutputStreamWriter(conn.getOutputStream())) {
                final JSONObject json = toJson(task.getCurrentWorkLog());
                json.write(writer);
            }
            final int resCode = conn.getResponseCode();
            if (resCode == 401){
                throw new AuthenticationRequiredException(this, this.auth.orElse(null));
            }
            else if(resCode != 201){
                final JSONObject errorJson = new JSONObject(new JSONTokener(conn.getErrorStream()));
                throw new IllegalArgumentException(resCode + ": " + errorJson);
            }
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private JSONObject toJson(final WorkLog workLog)
    {
        final JSONObject json = new JSONObject();
        Instant instant = Instant.ofEpochMilli(workLog.getTimer().getStartTime());
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(JIRA_TIME_PATTERN);
        String formattedDateTime = formatter.format(dateTime);
        json.put(WORKLOG_START_TIME_KEY, formattedDateTime);
        json.put(WORKLOG_DURATION_KEY, workLog.getTimer().getDuration() / 1000);
        json.put(WORKLOG_DESC_KEY, workLog.getComment());
        return json;
    }
}
