package com.testing4everyone.kafka.fraud.service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.testing4everyone.kafka.fraud.service.model.User;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Kafka_Example")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }


    @KafkaListener(topics = "Kafka_Example_json",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(User user) {
        System.out.println("Consumed JSON Message: " + user);
    }
}
