package com.dropwizardsample.resources;

import com.codahale.metrics.annotation.Timed;
import com.dropwizardsample.db.PersonDao;
import com.dropwizardsample.model.Person;
import com.dropwizardsample.utils.ResponseUtil;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private Validator validator;
    private Jdbi jdbi;

    public PersonResource(Jdbi jdbi, Validator validator) {
        this.validator = validator;
        this.jdbi = jdbi;
    }

    @RolesAllowed("ADMIN")
    @GET
    @Timed
    public Response getPersons() {
        List<Person> list = jdbi.withExtension(PersonDao.class, PersonDao::listPersons);
        if (list.size() > 0)
            return Response.ok(list).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("/{id}")
    @Timed
    public Response getPersonById( @PathParam("id") Integer id) {
        Optional<Person> person = Optional.ofNullable(jdbi.withExtension(PersonDao.class, dao ->
                dao.getPerson(id)));
        if (person.isPresent())
            return Response.ok(person.get()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createPerson(Person person) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (violations.size() > 0) {
            ResponseUtil.getBadRequestResponse(violations);
        }
        int id = jdbi.withExtension(PersonDao.class, dao -> dao.insertBean(person));
        if (id > 0)
            return Response.ok().build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removePersonById(@PathParam("id") Integer id) {
        long deletedId = jdbi.withExtension(PersonDao.class, dao -> dao.deletePerson(id));
        if (id > 0)
            return Response.ok().build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
