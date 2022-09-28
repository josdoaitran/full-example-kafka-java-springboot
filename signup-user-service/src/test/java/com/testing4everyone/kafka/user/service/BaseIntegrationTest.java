package com.testing4everyone.kafka.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ImportAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class BaseIntegrationTest extends TestCase {

    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;


    public BaseIntegrationTest() {
        initializeApplicationProperties();
    }

    public BaseIntegrationTest(String testName) {
        super(testName);
        initializeApplicationProperties();
    }

    public void initializeApplicationProperties() {
        this.objectMapper = new ObjectMapper();
    }
}
