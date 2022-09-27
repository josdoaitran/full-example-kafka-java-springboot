package com.testing4everyone.kafka.user.service.controller;

import com.testing4everyone.kafka.user.service.exception.ApiRequestException;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.model.UserStatus;
import com.testing4everyone.kafka.user.service.service.UserService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.kafka.support.SendResult;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    private NewTopic topic;
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String TOPIC = "CREATE_NEW_USER_TOPIC";

    @PostMapping("/sign-up")
    public ResponseEntity<User> addUser(@RequestBody UserSignUpForm user) {
        User createdUser;
        try {
            createdUser = userService.saveUser(new User(user.getname(), user.getPhone(), UserStatus.OPEN.toString()));
        }catch (Exception e){
            throw new ApiRequestException(e.getMessage());
        }
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        Message<User> message = MessageBuilder.withPayload(createdUser).setHeader(KafkaHeaders.TOPIC, TOPIC).build();
        kafkaTemplate.send(message);

        return ResponseEntity.created(uri).body(createdUser);
    }
    @GetMapping("/get_UserId/{UserId}")
    public ResponseEntity<Optional<User>> User(@PathVariable String UserId){
        HttpHeaders headers = new HttpHeaders();
        Optional<User> returnUser = userService.getUserById(UserId);
        return new ResponseEntity<>(returnUser, headers, HttpStatus.OK);
    }
}