package com.dropwizardsample.utils;

import com.dropwizardsample.model.Person;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Set;

public class ResponseUtil {
    public static Response getBadRequestResponse(Set<ConstraintViolation<Person>> violations){
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Person> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
    }
}
