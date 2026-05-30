package pl.edu.gamestore.encryption;

import org.springframework.stereotype.Component;

@Component
public class HashIdMapper {
    public Long toLong(HashId hashId) {
        return hashId == null ? null : hashId.value();
    }

    public HashId toHashId(Long value) {
        return value == null ? null : HashId.of(value);
    }
}
