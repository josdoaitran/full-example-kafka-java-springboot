package com.testing4everyone.kafka.user.service.stepdefs;

import com.testing4everyone.kafka.user.service.BaseIntegrationTest;
import com.testing4everyone.kafka.user.service.controller.UserSignUpForm;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDefs extends BaseIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(TestDefs.class);
    @Autowired
    UserRepository userRepository;


    @Given("^Clear User information in User Service by phone (.*)$")
    public void initTestData(String phone) {
        try{
            userRepository.deleteById(userRepository.findByPhone(phone).getId());
        }catch (Exception e){
            logger.info("No reset");
        }
    }

    @Given("^UserID has name (.*) phone (.*) and status (.*) in Signup User Service$")
    public void userInfoInSignupService(String name, String phone, String status) {
        User prepareUser = new User();
        prepareUser.setName(name);
        prepareUser.setPhone(phone);
        prepareUser.setStatus(status);
        userRepository.save(prepareUser);
    }

    MvcResult mvcResult;

    @When("^Request to get User information by phone (.*)$")
    public void getUserInformationByPhone(String phone) throws Exception {
        mvcResult = mockMvc.perform(get("/user/get_userid/" + phone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Then("^TestCase (.*): I expect API get User by phone (.*) will return name (.*) and status(.*)$")
    public void testcaseGetUserIdReturnResult(int testCaseNo, String phone, String name, String userStatus) throws Exception {
        User userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertNotNull(userResponse);
        assertEquals(userStatus.replace(" ",""), userResponse.getStatus());
        assertEquals(name, userResponse.getName());
        assertEquals(phone, userResponse.getPhone());
    }

    User signUpUserResponse;

    @Then("^TestCase (.*): I expect response message contain phone (.*) name (.*) status (.*)$")
    public void testPublishEmployee(String phone, String name, String userStatus) throws IOException, InterruptedException, ExecutionException {
        signUpUserResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        assertNotNull(signUpUserResponse);
        assertNotNull(signUpUserResponse.getId());
        assertEquals(name, signUpUserResponse.getName());
        assertEquals(phone, signUpUserResponse.getPhone());
        assertEquals(userStatus, signUpUserResponse.getName());

    }

    @Then("^I expect a new message Kafka topic (.*) phone (.*) name (.*) status (.*)$")
    public void testPublishSigupUserMesssage(String topicName, String phone, String name, String userStatus){

    }

    @Given("^User signup with name (.*) phone (.*)$")
    public void userSignUp(String name, String phone) throws Exception {
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUserSignUpRequest())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    private UserSignUpForm buildUserSignUpRequest() {
        return new UserSignUpForm("Doai Tran", "0906973152");
    }

}
