package com.socialmedia.backend.controller;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoTestController {

    private final MongoTemplate mongoTemplate;

    public MongoTestController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Connected to MongoDB: "
                + mongoTemplate.getDb().getName();
    }
}