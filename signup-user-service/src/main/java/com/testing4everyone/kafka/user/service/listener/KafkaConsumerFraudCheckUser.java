
package com.testing4everyone.kafka.user.service.listener;

import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.model.UserStatus;
import com.testing4everyone.kafka.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerFraudCheckUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerFraudCheckUser.class);
    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String UPDATE_USER_INFO_TOPIC = "UPDATE_USER_INFO_TOPIC";

    @KafkaListener(topics = UPDATE_USER_INFO_TOPIC, groupId = "group_json")
    public void consumeSigupUserMessage(User user) {
        LOGGER.info("Consumed User JSON Message: " + user.toString());
        // Save new sign-up user to check Fraud
        userService.saveUser(user);
        String newPhone = user.getPhone();
        String newUserId = String.valueOf(userService.getUserByPhone(newPhone).getId());
        LOGGER.info((user.getName().toString()));
        userService.updateUserStatus(newUserId, user.getStatus());
    }
}
