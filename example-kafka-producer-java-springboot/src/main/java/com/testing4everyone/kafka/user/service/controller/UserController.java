package com.testing4everyone.kafka.user.service.controller;

import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String TOPIC = "CREATE_NEW_USER_TOPIC";

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        kafkaTemplate.send(TOPIC, createdUser);
        return ResponseEntity.created(uri).body(createdUser);
    }
}