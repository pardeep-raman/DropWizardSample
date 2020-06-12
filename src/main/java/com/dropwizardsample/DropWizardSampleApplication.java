package com.dropwizardsample;

import com.dropwizardsample.client.PersonAuthorizer;
import com.dropwizardsample.client.PersonBasicAuthenticator;
import com.dropwizardsample.client.PersonOathAuthenticator;
import com.dropwizardsample.client.User;
import com.dropwizardsample.health.healthcheck.TemplateHealthCheck;
import com.dropwizardsample.resources.HelloWorldResource;
import com.dropwizardsample.resources.PersonResource;
import com.google.common.collect.Lists;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class DropWizardSampleApplication extends Application<DropWizardSampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropWizardSampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropWizardSample";
    }

    @Override
    public void initialize(final Bootstrap<DropWizardSampleConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DropWizardSampleConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final HelloWorldResource helloWorldResource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final PersonResource personResource= new PersonResource(jdbi,environment.getValidator());
        final TemplateHealthCheck templateHealthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", templateHealthCheck);
        environment.jersey().register(helloWorldResource);
        environment.jersey().register(personResource);

        AuthFilter basicCredentialAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new PersonBasicAuthenticator())
                .setAuthorizer(new PersonAuthorizer())
                .setRealm("Basic-Auth")
                .buildAuthFilter();

        AuthFilter oauthCredentialAuthFilter = new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new PersonOathAuthenticator())
                .setAuthorizer(new PersonAuthorizer())
                .setPrefix("Bearer")
                .buildAuthFilter();

        List<AuthFilter> filters = Lists.newArrayList(basicCredentialAuthFilter, oauthCredentialAuthFilter);
        environment.jersey().register(new AuthDynamicFeature(new ChainedAuthFilter<>(filters)));
//        environment.jersey().register(new AuthDynamicFeature(
//                new BasicCredentialAuthFilter.Builder<User>()
//                        .setAuthenticator(new PersonBasicAuthenticator())
//                        .setAuthorizer(new PersonAuthorizer())
//                        .setRealm("SUPER SECRET STUFF")
//                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

}
