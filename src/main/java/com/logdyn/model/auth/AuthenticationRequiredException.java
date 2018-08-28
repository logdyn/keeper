package com.logdyn.model.auth;

import com.logdyn.model.repositories.Repository;

public class AuthenticationRequiredException extends Exception{

    private final Authenticator auth;
    private final Repository repo;

    public AuthenticationRequiredException(final Repository repo) {
        this(repo, null);
    }

    public AuthenticationRequiredException(final Repository repo, final Authenticator auth) {
        super(String.format("Authentication for repository [%s] is required", repo.getName())); //NON-NLS
        this.auth = auth;
        this.repo = repo;
    }

    public Authenticator getAuthenticator() {
        return this.auth;
    }

    public Repository getRepository() {
        return this.repo;
    }
}
