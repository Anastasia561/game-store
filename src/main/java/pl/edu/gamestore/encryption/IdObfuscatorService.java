package pl.edu.gamestore.encryption;

public interface IdObfuscatorService {
    String encode(Long id);

    Long decode(String hash);
}
