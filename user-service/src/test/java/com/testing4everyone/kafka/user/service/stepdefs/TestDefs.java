package com.testing4everyone.kafka.user.service.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing4everyone.kafka.user.service.config.SpringAcceptanceTest;
import com.testing4everyone.kafka.user.service.controller.UserSignUpForm;
import com.testing4everyone.kafka.user.service.library.KafkaTestConsumerUtil;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Durations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDefs extends SpringAcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(TestDefs.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;


    @Given("^Clear User information in User Service by Phone = (.*)$")
    public void initTestData(String phone) {
        try{
            userRepository.deleteById(userRepository.findByPhone(phone).getId());
        }catch (Exception e){
            logger.info("No reset");
        }
    }

    @Given("^UserID = (.*) has Name = (.*) Phone = (.*) and Status = (.*) in User Service$")
    public void userInfoInSignupService(int id, String name, String phone, String status) {
        User prepareUser = new User();
        prepareUser.setId(id);
        prepareUser.setName(name);
        prepareUser.setPhone(phone);
        prepareUser.setStatus(status);
        userRepository.save(prepareUser);
    }


    private MvcResult mvcResult;

    @When("^Request to get User information by Phone = (.*)$")
    public void getUserInformationByPhone(String phone) throws Exception {
        mvcResult = mockMvc.perform(get("/user/get_userid/" + phone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Then("^TestCase (.*): I expect API get User by Phone = (.*) will return Name = (.*) and Status = (.*)$")
    public void testcaseGetUserIdReturnResult(int testCaseNo, String phone, String name, String userStatus) throws Exception {
        User userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(userResponse);
        userStatus.replace(" ","");
        userResponse.getStatus();
        assertThat(userStatus.replace(" ","")).isEqualTo(userResponse.getStatus());
        assertThat(name).isEqualTo(userResponse.getName());
        assertThat(phone).isEqualTo(userResponse.getPhone());
    }

    @Then("^TestCase (.*): I expect response message contain Phone = (.*) Name = (.*) Status = (.*)$")
    public void testPublishEmployee(String testcase, String phone, String name, String userStatus) throws IOException, InterruptedException, ExecutionException {
        User signUpUserResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        assertThat(signUpUserResponse);
        assertThat(userStatus.replace(" ","")).isEqualTo(signUpUserResponse.getStatus());
        assertThat(name).isEqualTo(signUpUserResponse.getName());
        assertThat(phone).isEqualTo(signUpUserResponse.getPhone());

    }

    private Consumer<String, User> consumerServiceTest;


    @Given("^Prepare consumer listen Topic = (.*)$")
    public void prepareConsumer(String topic){
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group_json", "false", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory cf = new DefaultKafkaConsumerFactory<String, User>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(User.class, false));
        consumerServiceTest = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerServiceTest, topic);
    }

    @Then("^I expect a new message Kafka topic (.*) Phone = (.*) Name = (.*) Status = (.*)$")
    public void testPublishSigupUserMesssage(String topicName, String phone, String name, String userStatus) {
        ConsumerRecord<String, User> consumerRecordOfExampleDTO = KafkaTestUtils.getSingleRecord(consumerServiceTest, topicName);
        User valueReceived = consumerRecordOfExampleDTO.value();

        assertThat(userStatus.replace(" ","")).isEqualTo(valueReceived.getStatus());
        assertThat(name).isEqualTo(valueReceived.getName());
        assertThat(phone).isEqualTo(valueReceived.getPhone());

        consumerServiceTest.close();
    }

    @Then("^I expect User Service consume update user message Id = (.*) Phone = (.*) Name = (.*) Status = (.*)$")
    public void testConsumerUpdateUserMesssage(int id, String phone, String name, String userStatus) {
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            User updatedUser = userRepository.findByPhone(phone);
            assertEquals(name, updatedUser.getName());
            assertEquals(phone, updatedUser.getPhone());
            assertEquals(userStatus, updatedUser.getStatus());

//            List<User> exampleEntityList = userRepository.findAll();
//            assertEquals(1, exampleEntityList.size());
//            User firstEntity = exampleEntityList.get(0);
//            assertEquals(name, firstEntity.getName());
//            assertEquals(phone, firstEntity.getPhone());
//            assertEquals(userStatus, firstEntity.getStatus());
        });
    }

    @Given("^User signup with Name = (.*) Phone = (.*)$")
    public void userSignUp(String name, String phone) throws Exception {
        UserSignUpForm buildUserSignUpRequest = new UserSignUpForm(name, phone);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUserSignUpRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    private Producer<String, User> producerTest;

    @Given("^Prepare producer message Topic = (.*)$")
    public void prepareProducer(String topic){
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker.getBrokersAsString());
        logger.info("props {}", producerProps);
        producerTest = new KafkaProducer(producerProps, new StringSerializer(), new JsonSerializer<User>());
    }


    @Given("^There is a new Update User Status message to Topic (.*) Id = (.*) Phone = (.*) Name = (.*) Status = (.*) from Fraud Service$")
    public void newUpdateUserStatusMessage(String topic, int id, String phone, String name, String status){
        User userMessage = new User(id, name, phone, status);
        producerTest.send(new ProducerRecord(topic, "", userMessage));
        producerTest.close();
    }


}
