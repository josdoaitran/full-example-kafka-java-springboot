package com.testing4everyone.kafka.user.service.controller;

import com.testing4everyone.kafka.user.service.exception.ApiRequestException;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.model.UserStatus;
import com.testing4everyone.kafka.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.kafka.support.SendResult;

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

        ListenableFuture<SendResult<String,User>> future = kafkaTemplate.send(TOPIC, createdUser);
        future.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Messages failed to push on topic");
            }

            @Override
            public void onSuccess(Object result) {
                System.out.println("Messages successfully pushed on topic");
            }
        });

//        kafkaTemplate.send(TOPIC, createdUser);

        return ResponseEntity.created(uri).body(createdUser);
    }
}