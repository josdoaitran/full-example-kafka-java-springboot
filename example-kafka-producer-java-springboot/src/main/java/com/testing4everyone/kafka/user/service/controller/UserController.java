package com.testing4everyone.kafka.user.service.controller;

import com.testing4everyone.kafka.user.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("kafka")
public class UserController {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String TOPIC = "PUBLISH_NAME_TOPIC";

    @GetMapping("/publish/{name}")
    public User post(@RequestBody User user) {
        User userCall = new User(user.getName(), user.getDept(), user.getSalary());
        kafkaTemplate.send(TOPIC, userCall);
        return userCall;
    }
}
