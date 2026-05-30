package pl.edu.gamestore.config;

import org.springframework.stereotype.Component;
import pl.edu.gamestore.encryption.HashId;

@Component
public class HashIdMapper {
    public Long toLong(HashId hashId) {
        return hashId == null ? null : hashId.value();
    }

    public HashId toHashId(Long value) {
        return value == null ? null : HashId.of(value);
    }
}
