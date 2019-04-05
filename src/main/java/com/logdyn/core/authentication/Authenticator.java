package com.logdyn.core.authentication;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.net.HttpURLConnection;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({@JsonSubTypes.Type(BasicAuthenticator.class)})
public interface Authenticator {
    void authenticate(final HttpURLConnection connection);
}
