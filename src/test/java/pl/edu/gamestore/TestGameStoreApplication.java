package pl.edu.gamestore;

import org.springframework.boot.SpringApplication;

public class TestGameStoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(GameStoreApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
