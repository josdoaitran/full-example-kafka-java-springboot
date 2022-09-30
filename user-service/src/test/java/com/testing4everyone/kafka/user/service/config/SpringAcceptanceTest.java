package com.testing4everyone.kafka.user.service.config;

import com.testing4everyone.kafka.user.service.UserServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@CucumberContextConfiguration
@SpringBootTest(classes = {UserServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"kafka-embedded"})
@Tag("AcceptanceTest")
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"CREATE_NEW_USER_TOPIC"}, controlledShutdown = true,
    brokerProperties = {"log.dir=build/tmp/kafka-data/${spring.application.name}"})
public class SpringAcceptanceTest {
    @Autowired
    protected MockMvc mockMvc;
}
