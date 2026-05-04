package pl.edu.gamestore.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.gamestore.person.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
}
