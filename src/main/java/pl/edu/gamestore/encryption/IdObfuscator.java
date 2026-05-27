package pl.edu.gamestore.encryption;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdObfuscator {
    private static final int MIN_HASH_LENGTH = 8;

    private final Hashids hashids;

    public IdObfuscator(@Value("${app.salt}") String salt) {
        this.hashids = new Hashids(salt, MIN_HASH_LENGTH);
    }
    public String encode(Long id) {
        if (id == null) return null;
        return hashids.encode(id);
    }

    public Long decode(String hash) {
        if (hash == null || hash.isEmpty()) return null;
        long[] decoded = hashids.decode(hash);
        if (decoded.length == 0) {
            throw new IllegalArgumentException("Invalid obscured ID format.");
        }
        return decoded[0];
    }
}