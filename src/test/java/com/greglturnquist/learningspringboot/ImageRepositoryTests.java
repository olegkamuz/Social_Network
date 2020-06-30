package com.greglturnquist.learningspringboot;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageRepositoryTests {
    ImageRepository repository;

    MongoOperations operations;

    public void setUp() {

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
    }

    public void findAllShouldWork() {
        List<Image> images = repository.findAll().collectList().block();

        assertThat(images).hasSize(3);
        assertThat(images)
                .extracting(Image::getName)
                .contains(
                        "learning-spring-boot-cover.jpg",
                        "learning-spring-boot-2nd-edition-cover.jpg",
                        "bazinga.png");
    }

    public void findByNameShouldWork() {
        Image image = repository.findByName("bazinga.png").block();

        assertThat(image.getName()).isEqualTo("bazinga.png");
        assertThat(image.getId()).isEqualTo("3");
    }
}
