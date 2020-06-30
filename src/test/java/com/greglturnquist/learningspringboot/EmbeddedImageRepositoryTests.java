package com.greglturnquist.learningspringboot;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests extends LiveImageRepositoryTests {

    @Autowired
    ImageRepository repository;

    @Autowired
    MongoOperations operations;


    @Before
    public void setUp() {
        super.operations = operations;
        super.repository = repository;
        super.setUp();
    }

    @Test
    public void findAllShouldWork() {
        super.findAllShouldWork();
    }

    @Test
    public void findByNameShouldWork() {
        super.findByNameShouldWork();
    }

}

