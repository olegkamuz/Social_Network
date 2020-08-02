package com.greglturnquist.learningspringboot.chapters;

import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

//@Configuration
public class LoadDatabase {

//    @Bean
    CommandLineRunner init(ChapterRepository repository) {
        return args -> {
            Flux.just (
                    new Chapter("Quick Start with Java"),
                    new Chapter("Reactive Web with Spring Boot"),
                    new Chapter("...and more"))
                    .flatMap(repository::save)
                    .subscribe(System.out::println);
        };
    }
}
