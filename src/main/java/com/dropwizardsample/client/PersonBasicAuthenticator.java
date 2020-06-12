package com.dropwizardsample.client;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class PersonBasicAuthenticator implements Authenticator<BasicCredentials, User> {
    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if ("secret".equals(basicCredentials.getPassword())) {
            return Optional.of(new User(basicCredentials.getUsername()) {
            });
        }
        return Optional.empty();
    }
}
