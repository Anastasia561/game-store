package pl.edu.gamestore.person.service;

import pl.edu.gamestore.person.model.Person;

public interface PersonService {
    Person getByEmail(String email);
}
