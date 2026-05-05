package pl.edu.gamestore.person.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.gamestore.person.model.Person;
import pl.edu.gamestore.person.repository.PersonRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    void getByEmail_ShouldReturnPerson_WhenEmailExists() {
        String expectedEmail = "user@example.com";
        Person mockPerson = new Person();
        mockPerson.setEmail(expectedEmail);

        when(personRepository.findByEmail(expectedEmail)).thenReturn(Optional.of(mockPerson));

        Person result = personService.getByEmail(expectedEmail);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(expectedEmail);
        verify(personRepository).findByEmail(expectedEmail);
    }

    @Test
    void getByEmail_ShouldThrowException_WhenEmailDoesNotExist() {
        String email = "missing@example.com";
        when(personRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.getByEmail(email))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Person not found");
    }

    @Test
    void getByEmail_ShouldConvertEmailToLowerCase_whenInputIsMixed() {
        String inputEmail = "UPPERCASE@domain.com";
        String lowerCaseEmail = "uppercase@domain.com";

        when(personRepository.findByEmail(lowerCaseEmail)).thenReturn(Optional.of(new Person()));

        personService.getByEmail(inputEmail);

        verify(personRepository).findByEmail(lowerCaseEmail);
        verify(personRepository, never()).findByEmail(inputEmail);
    }
}
