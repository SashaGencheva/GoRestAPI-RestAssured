package tests;

import api.BaseRequest;
import api.PostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import data.Users;
import helper.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CreateUserTest extends Hooks {
    File jsonData;
    ObjectMapper mapper;
    Users user;
    Users userResponseObject;
    Faker faker;
    HashMap<String, String> headerMap;
    Response response;

    int actualStatusCode;
    String actualStatusLine;
    String actualHeaderValue;
    int actualId;
    String actualName;
    String actualEmail;
    String actualGender;
    String actualStatus;
    int expectedId;
    String expectedName;
    String expectedEmail;
    String expectedGender;
    String expectedStatus;

    @Test
    public void createUserTest(ITestContext context) throws IOException {
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress(name.replace(" ", "_"));

        //Jackson API
        mapper = new ObjectMapper();
        user = new Users(name, email, "male", "active"); //expected Users object

        //Object to json file
        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");
        mapper.writeValue(jsonData, user);

        //Java object to json in String (serialization)
        String userJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user); //request body

        //Call the API
        response = PostRequest.post(url, userJsonString, headerMap);
        response.then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
        //response.then().log().all();

        //Response to JSONObject type
        JSONObject responseJson = new JSONObject(response.asString());
        System.out.println("=================== Create User ===================");
        System.out.println("POST Request: " + url);

        System.out.println(BaseRequest.getResponseStatusLine(response));
        System.out.println("Response JSON from API: " + responseJson);


        actualId = response.jsonPath().getInt("id");
        System.out.println("Created user ID: " + actualId);

        //API Chaining
        context.getSuite().setAttribute("userId", actualId);

        user.setId(actualId);
        mapper.writeValue(jsonData, user);

        //Json to java object (deserialization)
        userResponseObject = mapper.readValue(response.asString(), Users.class); //actual Users object

        actualName = userResponseObject.getName();
        actualEmail = userResponseObject.getEmail();
        actualGender = userResponseObject.getGender();
        actualStatus = userResponseObject.getStatus();

        expectedId = user.getId();
        expectedName = user.getName();
        expectedEmail = user.getEmail();
        expectedGender = user.getGender();
        expectedStatus = user.getStatus();

        actualStatusCode = BaseRequest.getResponseCode(response);
        actualStatusLine = BaseRequest.getResponseStatusLine(response);
        actualHeaderValue = BaseRequest.getResponseHeaderValue(response, "Content-Type");

        Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_201, "Response status code is not 201");
        Assert.assertTrue(actualStatusLine.contains("Created"), "Response reason phrase is not 'Created'");
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        Assert.assertEquals(actualId, expectedId, "Expected and actual created user id do not match");
        Assert.assertEquals(actualName, expectedName, "Expected and actual created user name do not match");
        Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual created user email do not match");
        Assert.assertEquals(actualGender, expectedGender, "Expected and actual created user gender do not match");
        Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual created user status do not match");

        System.out.println("===================================================\n");
    }
}