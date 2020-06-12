package com.dropwizardsample.client;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class PersonOathAuthenticator implements Authenticator<String, User> {
    @Override
    public Optional<User> authenticate(String string) throws AuthenticationException {
        if ("pardeepPersonAuthToken".equals(string)) {
            return Optional.of(new User("Pardeep") {
            });
        }
        return Optional.empty();
    }
}
