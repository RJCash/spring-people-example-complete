package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by rickiecashwell on 4/13/17.
 */
@Component
public class PeopleRepository {
    @Autowired
    JdbcTemplate template;

    public List<Person> listPeople(String name) {
        return template.query("SELECT * FROM person Where lower(firstname) LIKE lower(?) OR lower(lastname) LIKE lower(?) order by personid " + "LIMIT 200"
                ,
                new Object[]{"%" + name + "%", "%" + name + "%"},
                (ResultSet, row) -> new Person(
                        ResultSet.getInt("personid"),
                        ResultSet.getString("title"),
                        ResultSet.getString("firstname"),
                        ResultSet.getString("middlename"),
                        ResultSet.getString("lastname"),
                        ResultSet.getString("suffix")
                )
        );
    }

    public Person specificPerson(Integer id) {
        return template.queryForObject("SELECT * FROM person             Where personid=?",
                new Object[]{id},
                (ResultSet, row) -> new Person(
                        ResultSet.getInt("personid"),
                        ResultSet.getString("title"),
                        ResultSet.getString("firstname"),
                        ResultSet.getString("middlename"),
                        ResultSet.getString("lastname"),
                        ResultSet.getString("suffix")
                )
        );
    }

    public void savePerson(Person person) {
        if (person.getId() == null) {
            template.update("INSERT INTO person(title,firstname,middlename,lastname,suffix) " +
                            "VALUES (?,?,?,?,?)",
                    new Object[]{person.getTitle(), person.getFirstName(), person.getMiddleName(),
                            person.getLastName(), person.getSuffix()});
            System.out.println("created");
        } else
            template.update("UPDATE person SET title = ?,firstname=?,middlename=?,lastname=?,suffix=? WHERE personid =?",
                    new Object[]{
                            person.getTitle(), person.getFirstName(), person.getMiddleName(),
                            person.getLastName(), person.getSuffix(), person.getId()});
        System.out.println("Updated");
    }


}


