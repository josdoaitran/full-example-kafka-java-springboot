
package com.testing4everyone.kafka.user.service.listener;

import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.model.UserStatus;
import com.testing4everyone.kafka.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerSignUpUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerSignUpUser.class);
    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String CREATE_NEW_USER_TOPIC = "CREATE_NEW_USER_TOPIC";
    private static final String UPDATE_USER_INFO_TOPIC = "UPDATE_USER_INFO_TOPIC";

    @KafkaListener(topics = CREATE_NEW_USER_TOPIC, groupId = "group_json")
    public void consumeSigupUserMessage(User user) {
        LOGGER.info("Consumed User JSON Message: " + user.toString());
        // Save new sign-up user to check Fraud
        userService.saveUser(user);
        String newPhone = user.getPhone();
        String newUserId = String.valueOf(userService.getUserByPhone(newPhone).getId());

        if(newPhone.contains("089")){
            // Update Status to Blocked
            userService.updateUserStatus(newUserId, UserStatus.BLOCKED.toString());
            user.setStatus(UserStatus.BLOCKED.toString());
            // Push Kafka message
            Message<User> message = MessageBuilder.withPayload(user).setHeader(KafkaHeaders.TOPIC, UPDATE_USER_INFO_TOPIC).build();
            kafkaTemplate.send(message);
        }else{
            // Update Status to PENDING
            userService.updateUserStatus(newUserId, UserStatus.PENDING.toString());
            user.setStatus(UserStatus.PENDING.toString());
            // Push Kafka message
            Message<User> message = MessageBuilder.withPayload(user).setHeader(KafkaHeaders.TOPIC, UPDATE_USER_INFO_TOPIC).build();
            kafkaTemplate.send(message);
        }

    }
}
