package com.dropwizardsample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Person {
    @NotNull
    private int id;
    @NotNull
    @Length(min = 2, max = 100)
    private String name;
    @NotNull
    @Length(min = 10, max = 13)
    private String phonenumber;
    @NotNull
    private String address;
    @NotNull
    @Pattern(regexp = ".+@.+\\.[a-z]+")
    private String email;

    public Person() {
    }

    public Person(int id, String name, String phonenumber, String address, String email) {
        this.id = id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.email = email;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getPhonenumber() {
        return phonenumber;
    }

    @JsonProperty
    public String getAddress() {
        return address;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
