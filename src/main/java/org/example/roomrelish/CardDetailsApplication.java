package org.example.roomrelish;

import org.jetbrains.annotations.TestOnly;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@TestOnly
public class CardDetailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardDetailsApplication.class, args);
    }

}
