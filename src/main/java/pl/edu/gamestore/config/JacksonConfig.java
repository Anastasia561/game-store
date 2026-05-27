package pl.edu.gamestore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.encryption.HashIdDeserializer;
import pl.edu.gamestore.encryption.HashIdSerializer;
import tools.jackson.databind.module.SimpleModule;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    private final HashIdSerializer hashIdSerializer;
    private final HashIdDeserializer hashIdDeserializer;

    @Bean
    public SimpleModule hashidsModule() {
        SimpleModule module = new SimpleModule("HashidsModule");

        module.addSerializer(HashId.class, hashIdSerializer);
        module.addDeserializer(HashId.class, hashIdDeserializer);

        return module;
    }
}