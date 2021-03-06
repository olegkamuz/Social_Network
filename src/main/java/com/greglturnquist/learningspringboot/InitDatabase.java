package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.images.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class InitDatabase {
    @Bean
	CommandLineRunner init(MongoOperations operations) {
        return args -> {
            operations.dropCollection(Image.class);

            operations.insert(new Image("1",
                    "learning-spring-boot-cover.jpg"));
            operations.insert(new Image("2",
                    "learning-spring-boot-2nd-edition-cover.jpg"));
            operations.insert(new Image("3",
                    "bazinga.png"));

            operations.findAll(Image.class).forEach(image -> {
                System.out.println(image.toString());
            });
        };
    }
}