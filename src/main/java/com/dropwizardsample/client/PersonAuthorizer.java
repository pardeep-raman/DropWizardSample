package com.dropwizardsample.client;

import io.dropwizard.auth.Authorizer;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;

public class PersonAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String s) {
        return user.getName().equals("pardeep") && s.equals("ADMIN");
    }

    @Override
    public boolean authorize(User principal, String role, @Nullable ContainerRequestContext requestContext) {
        return principal.getName().equals("pardeep") && role.equals("ADMIN");
    }
}
