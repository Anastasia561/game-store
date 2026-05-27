package pl.edu.gamestore.person;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
