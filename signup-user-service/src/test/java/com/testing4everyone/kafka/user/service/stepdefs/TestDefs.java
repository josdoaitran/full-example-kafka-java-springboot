package com.testing4everyone.kafka.user.service.stepdefs;

import com.testing4everyone.kafka.user.service.BaseIntegrationTest;
import com.testing4everyone.kafka.user.service.controller.UserSignUpForm;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import cucumber.api.java.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDefs extends BaseIntegrationTest {

    @Autowired
    UserRepository userRepository;

    User signUpUserResponse;

    @Before
    public void initTestData() throws Exception {
        clearData();
    }

    private void clearData() {
        userRepository.deleteAll();
    }

//    private MvcResult getMvcResult() throws Exception {
//        return this.mockMvc.perform(post("/user/sign-up")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(buildUserSignUpRequest())))
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//    }
//
//    private UserSignUpForm buildUserSignUpRequest() {
//        return new UserSignUpForm("Doai Tran", "0906973152");
//    }

    @Given("^UserID has name (.*) phone (.*) and status (.*) in Signup User Service$")
    public void userInfoInSignupService(String name, String phone, String status) {
        User prepareUser = new User();
        prepareUser.setName(name);
        prepareUser.setPhone(phone);
        prepareUser.setStatus(status);
        userRepository.save(prepareUser);
    }

    @Then("^TestCase (.*): I expect API get User by phone (.*) will return name (.*) and status(.*)$")
    public void testcaseGetUserIdReturnResult(int testCaseNo, String phone, String name, String userStatus) throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/user/get_userid/" + phone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        User userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        assertNotNull(userResponse);
        assertEquals(userStatus.replace(" ",""), userResponse.getStatus().toString());
    }

}
