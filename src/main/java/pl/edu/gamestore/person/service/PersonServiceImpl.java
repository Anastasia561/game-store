package pl.edu.gamestore.person.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.person.model.Person;
import pl.edu.gamestore.person.repository.PersonRepository;

@Service
@RequiredArgsConstructor
class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Person getByEmail(String email) {
        return personRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }
}
