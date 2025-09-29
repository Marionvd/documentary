package com.mariodoumbanov.documentqa;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocumentQaApplication {

    public static void main(String[] args) {
        var dotenv = Dotenv.load();
        System.setProperty("spring.datasource.url", dotenv.get("DATABASE_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));
        SpringApplication.run(DocumentQaApplication.class, args);
    }

}
