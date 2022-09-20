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

    private static final String TOPIC = "CREATE_NEW_USER_TOPIC";

    @PostMapping("/publish/")
    public String post(@RequestBody final User user) {
//        kafkaTemplate.send(TOPIC, );
        return "Published successfully";
    }
}
