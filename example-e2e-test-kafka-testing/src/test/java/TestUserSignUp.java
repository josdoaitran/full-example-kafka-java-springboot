import com.google.gson.Gson;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import org.apache.kafka.common.serialization.StringDeserializer;
import models.User;
import org.apache.kafka.clients.consumer.*;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;

public class TestUserSignUp {

    KafkaConsumer<String, String> consumer;
    private static String userServiceDatabase = GetDataProperties.getDataKey("user-service-db.url");
    private static String fraudServiceDatabase = GetDataProperties.getDataKey("fraud-check-service-db.url");
    private static String userDb = GetDataProperties.getDataKey("db.user");
    private static String passwordDb = GetDataProperties.getDataKey("db.password");
    private static String BaseUrl = GetDataProperties.getDataKey("test-service");

    Response response;

    @Given("^Clear User information in User Service by Phone = (.*)$")
    public void clearPhone(String phone) throws ClassNotFoundException, SQLException {
        String statement = String.format("Delete from user where phone = '%s'", phone);
        ConnectDatabase.deleteDatabase(userServiceDatabase, userDb, passwordDb, statement);
        ConnectDatabase.deleteDatabase(fraudServiceDatabase, userDb, passwordDb, statement);
    }

    @And("^Validate User info in (.*) correct Phone = (.*) Name = (.*) Status = (.*)$")
    public void checkDatabase(String service, String phone, String name, String status) throws ClassNotFoundException, SQLException {
        String statement = String.format("Select * from user where phone = '%s'", phone);
        ResultSet rs;
        if (service.equalsIgnoreCase("User-Service")){
            rs = ConnectDatabase.checkDatabase(userServiceDatabase, userDb, passwordDb, statement);
        }else{
            rs = ConnectDatabase.checkDatabase(fraudServiceDatabase, userDb, passwordDb, statement);
        }

        while (rs.next()) {
            Assert.assertEquals(name, rs.getString("name"));
            Assert.assertEquals(phone, rs.getString("phone"));
            Assert.assertEquals(status, rs.getString("status"));
        }
    }

    @Given("^User signup with Name = (.*) Phone = (.*)$")
    public void userSignUp(String name, String phone) throws Exception {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", name);
        requestParams.put("phone", phone);

        response = given()
                .baseUri(BaseUrl)
                .log().everything()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
                .when()
                .post("/user/sign-up");
    }

    @Then("^I expect response message contain StatusCode = (.*) Phone = (.*) Name = (.*) Status = (.*)$")
    public void testPublishEmployee(int code, String phone, String name, String userStatus) throws IOException, InterruptedException, ExecutionException {
        ResponseBody body = response.getBody();
        Assert.assertEquals(code, response.getStatusCode());
        Assert.assertEquals(phone, body.jsonPath().get("phone"));
        Assert.assertEquals(name, body.jsonPath().get("name"));
        Assert.assertEquals(userStatus, body.jsonPath().get("status"));
    }

    @Given("^User get user information by Phone = (.*)$")
    public void getUserInfo(String phone) throws Exception {
        response = null;
        response = given()
                .baseUri(BaseUrl)
                .log().everything()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/get_userid/" + phone);
    }


    @And("^I prepare a listener topic (.*)$")
    public void createListener(String topic) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));
    }

    private User getLastedMessage() throws InterruptedException {
        User userData = null;
        int i = 0;
        while (i<=5) {

            ConsumerRecords<String, String> records = consumer.poll(1000);
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, String> record : records) {

                    System.out.println(("Key: " + record.key() + ", Value:" + record.value()));
                    System.out.println("Partition:" + record.partition() + ",Offset:" + record.offset());
                    Gson g = new Gson();
                    userData = g.fromJson(String.valueOf(record.value()), User.class);
                    consumer.commitAsync();
                    consumer.close();
                    return userData;

                }
            }else {
                i++;
                Thread.sleep(1000);
            }

        }
        consumer.close();
        return null;
    }

    @Then("^I expect a new message Kafka topic (.*) Phone = (.*) Name = (.*) Status = (.*)$")
    public void testPublishSigupUserMesssage(String topicName, String phone, String name, String userStatus) throws InterruptedException {
        User userData = getLastedMessage();
        Assert.assertEquals(phone, userData.getPhone());
        Assert.assertEquals(name, userData.getName());
        Assert.assertEquals(userStatus, userData.getStatus());
        System.out.println("Done");
    }
}

