package com.testing4everyone.kafka.user.service;

import com.testing4everyone.kafka.user.service.controller.UserController;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import com.testing4everyone.kafka.user.service.service.UserService;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserRepositoryTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        User user = new User("alex", "0906973152", "OPEN");


        given(service.saveUser(user)).willReturn(allEmployees);

        mvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].name", is(alex.getName()))).andExpect(jsonPath("$[1].name", is(john.getName())))
                .andExpect(jsonPath("$[2].name", is(bob.getName())));
        verify(service, VerificationModeFactory.times(1)).getAllEmployees();
        reset(service);
    }



}
