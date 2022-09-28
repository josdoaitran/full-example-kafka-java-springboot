package com.testing4everyone.kafka.user.service.stepdefs;

import com.testing4everyone.kafka.user.service.BaseIntegrationTest;
import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import cucumber.api.java.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.*;

import org.springframework.kafka.test.rule.EmbeddedKafkaRule;


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDefs extends BaseIntegrationTest {

    static KafkaContainer kafka;

    static {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
    }

    @Autowired
    private KafkaAdmin admin;

    @Autowired
    UserRepository userRepository;

    User signUpUserResponse;

    @Before
    public void initTestData() throws Exception {
        clearData();
        setUpKafka();
    }

    private void clearData() {
        userRepository.deleteAll();
    }

    private static final String TOPIC_NAME = "CREATE_NEW_USER_TOPIC";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC_NAME);
    private void setUpKafka(){
        System.setProperty("spring.cloud.stream.kafka.binder.brokers",
                embeddedKafka.getEmbeddedKafka()
                        .getBrokersAsString());
        System.setProperty("spring.cloud.stream.bindings.input.destination", TOPIC_NAME);
        System.setProperty("spring.cloud.stream.bindings.input.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.bindings.input.group", "group_json");
        System.setProperty("spring.cloud.stream.bindings.output.destination", TOPIC_NAME);
        System.setProperty("spring.cloud.stream.bindings.output.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.bindings.output.group", "output-group-1");
    }

    @Test
    public void testCreationOfTopicAtStartup() throws IOException, InterruptedException, ExecutionException {
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        Collection<TopicListing> topicList = client.listTopics().listings().get();
        assertNotNull(topicList);
        assertEquals(topicList.stream().map(l -> l.name()).collect(Collectors.toList()), Arrays.asList("create-employee-events","springboot-topic"));
    }


    @Then("^TestCase (.*): I expect a new message Kafka topic (.*) phone (.*) username (.*) status (.*)$")
    public void testPublishEmployee(int testCaseNo, String topicName, String phone, String name, String userStatus) throws IOException, InterruptedException, ExecutionException {
        // first create the create-employee-events topic

        NewTopic topic1 =  TopicBuilder.name(topicName).build();

        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        client.createTopics( Collections.singletonList(topic1));

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        props.put(JsonDeserializer.TYPE_MAPPINGS, "Employee:dev.fullstackcode.kafka.producer.dto.Employee");

        KafkaConsumer<Integer, User> consumer = new KafkaConsumer(props);

        consumer.subscribe(Collections.singletonList(TOPIC_NAME));


        Collection<TopicListing> topicList = client.listTopics().listings().get();
        assertEquals(topicList.size(),2);
        List<String> topicNameList = topicList.stream().map(l -> l.name()).collect(Collectors.toList());
        List<String> expectedTopicNameList =  Arrays.asList(TOPIC_NAME);
        assertTrue(topicNameList.containsAll(expectedTopicNameList) && expectedTopicNameList.containsAll(topicNameList));


        await().atMost(20, TimeUnit.SECONDS).until(() -> {
            ConsumerRecords<Integer, User> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }
            records.forEach( r -> System.out.println(r.topic() + " *** "+ r.key() + " *** "+ r.value()));
            Assertions.assertThat(records.count()).isEqualTo(1);
            Assertions.assertThat(records.iterator().next().value().getName()).isEqualTo(name);
            Assertions.assertThat(records.iterator().next().value().getStatus()).isEqualTo(status());
            return true;
        });

    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.properties.bootstrap.servers",kafka::getBootstrapServers);
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
