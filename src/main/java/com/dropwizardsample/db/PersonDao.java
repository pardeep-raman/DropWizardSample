package com.dropwizardsample.db;

import com.dropwizardsample.model.Person;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

// Define your own declarative interface

public interface PersonDao {
    @SqlUpdate("CREATE TABLE PERSON (id INTEGER PRIMARY KEY, name VARCHAR, phonenumber VARCHAR, address VARCHAR, email VARCHAR)")
    void createTable();

    @SqlUpdate("INSERT INTO PERSON(id, name, phonenumber, address, email) VALUES (?, ?)")
    void insertPositional(int id, String name, String phonenumber, String address, String email);

//    @SqlUpdate("INSERT INTO person(id, name, phonenumber, address, email) VALUES (:id, :name, :phonenumber, :address, :email)")
//    void insertNamed(@Bind("id") int id, @Bind("name") String name);

    @SqlUpdate("INSERT INTO PERSON(id, name, phonenumber, address, email) VALUES (:id, :name, :phonenumber, :address, :email)")
    int insertBean(@BindBean Person person);

    @SqlQuery("SELECT * FROM PERSON ORDER BY name")
    @RegisterBeanMapper(Person.class)
    List<Person> listPersons();

    @SqlQuery("SELECT * FROM PERSON WHERE id= :id ORDER BY name")
    @RegisterBeanMapper(Person.class)
    Person getPerson(@Bind("id") int id);

    @SqlQuery("DELETE FROM PERSON WHERE id= :id")
    long deletePerson(@Bind("id") int id);
}