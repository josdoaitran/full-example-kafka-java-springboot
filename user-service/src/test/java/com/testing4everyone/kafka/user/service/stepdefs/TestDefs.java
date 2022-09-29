package com.testing4everyone.kafka.user.service.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing4everyone.kafka.user.service.config.SpringAcceptanceTest;
import com.testing4everyone.kafka.user.service.controller.UserSignUpForm;
import com.testing4everyone.kafka.user.service.library.KafkaTestConsumerUtil;
import com.testing4everyone.kafka.user.service.library.kafka.UserConsumer;
import com.testing4everyone.kafka.user.service.library.rest.TestRestClient;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;

import io.cucumber.java.en.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TestDefs extends SpringAcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(TestDefs.class);

    @Autowired
    private TestRestClient testRestClient;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserConsumer userConsumer;

    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;


    @Given("Clear User information in User Service by Phone {string}")
    public void clear_user_information_in_user_service_by_phone(java.lang.String phone) {
        try{
            userRepository.deleteById(userRepository.findByPhone(phone).getId());
        }catch (Exception e){
            logger.info("No reset^");
        }
    }

    @Given("UserID has Name {string} Phone {string} and Status {string} in Signup User Service")
    public void userInfoInSignupService(String name, String phone, String status) {
        User prepareUser = new User();
        prepareUser.setName(name);
        prepareUser.setPhone(phone);
        prepareUser.setStatus(status);
        userRepository.save(prepareUser);
    }

    MvcResult mvcResult;

    @When("Request to get User information by Phone {string}")
    public void getUserInformationByPhone(String phone) throws Exception {
        mvcResult = mockMvc.perform(get("/user/get_userid/" + phone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Then("I expect API get User by Phone {string} will return Name {string} and Status {string}")
    public void testcaseGetUserIdReturnResult(String phone, String name, String userStatus) throws Exception {
        User userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertNotNull(userResponse);
        assertEquals(userStatus.replace(" ",""), userResponse.getStatus());
        assertEquals(name, userResponse.getName());
        assertEquals(phone, userResponse.getPhone());
    }

    User signUpUserResponse;

    @Then("I expect response message contain Phone {string} Name {string} Status {string}")
    public void testPublishEmployee(String phone, String name, String userStatus) throws IOException, InterruptedException, ExecutionException {
        signUpUserResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        assertNotNull(signUpUserResponse);
        assertNotNull(signUpUserResponse.getId());
        assertEquals(name, signUpUserResponse.getName());
        assertEquals(phone, signUpUserResponse.getPhone());
        assertEquals(userStatus, signUpUserResponse.getName());

    }

    @Then("I expect a new message Kafka topic {string} Phone {string} Name {string} Status {string}")
    public void testPublishSigupUserMesssage(String topicName, String phone, String name, String status){

    }

    @Given("User signup with Name {string} Phone {string}")
    public void userSignUp(String name, String phone) throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUserSignUpRequest())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    private UserSignUpForm buildUserSignUpRequest() {
        return new UserSignUpForm("Doai Tran", "0906973152^");
    }


    @When("A user with phone={string} and name {string} is created")
    public void aUserWithIdAndNameIsCreated(String phone, String name) {
        final User user = User.builder()
                .phone(phone)
                .name(name)
                .build();

        testRestClient.saveUser(user);
    }

    @Then("The user with phone={string} and name {string} should be written on the topic")
    public void theUserWithIdAndNameShouldBeWrittenOnTheTopic(String phone, String name) {
        final Consumer<String, User> consumer = this. userConsumer.getUserConsumer();
        final ConsumerRecord<String, User> record = KafkaTestConsumerUtil.consume(phone, consumer, UserConsumer.USER_TOPIC);

        assertThat(record).isNotNull();
        User user = record.value();
        assertThat(user.getPhone()).isEqualTo(phone);
        assertThat(user.getName()).isEqualTo(name);
    }


}
