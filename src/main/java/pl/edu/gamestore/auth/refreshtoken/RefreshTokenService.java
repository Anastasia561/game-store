package pl.edu.gamestore.auth.refreshtoken;

import pl.edu.gamestore.person.Person;

public interface RefreshTokenService {

    void create(Person person, String tokenValue);

    void revoke(String tokenValue);
}
