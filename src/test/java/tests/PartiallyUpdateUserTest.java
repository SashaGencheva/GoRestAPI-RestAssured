package tests;

import api.BaseRequest;
import api.PatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class PartiallyUpdateUserTest extends Hooks {
    File jsonData;
    ObjectMapper mapper;
    Users user;
    HashMap<String, String> headerMap;
    Response response;

    int actualStatusCode;
    String actualStatusLine;
    String actualHeaderValue;
    int id;
    int actualId;
    String actualName;
    String actualEmail;
    String actualGender;
    String actualStatus;
    String actualMessage;
    String expectedName;
    String expectedEmail;
    String expectedGender;
    String expectedStatus;

    @Test
    public void partiallyUpdateUserTest(ITestContext context) throws IOException {

        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        //API Chaining
        id = (int) context.getSuite().getAttribute("userId");

        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class); //expected Users object

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            System.out.println("User details before PATCH request: " + user.getId() + ", " + user.getName() + ", " + user.getEmail() + ", " + user.getGender() + ", " + user.getStatus());
            user.setStatus("active");
            mapper.writeValue(jsonData, user);
        }

        //Java object to json in String (serialization)
        String userJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user); //request body

        //Call the API
        response = PatchRequest.patch(url + "/" + id, userJsonString, headerMap);
        //response.then().log().all();

        //JsonString to JSONObject
        JSONObject responseJson = new JSONObject(response.asString());
        System.out.println("============== Partially Update User ==============");
        System.out.println("PATCH Request: " + url + "/" + id);

        System.out.println(BaseRequest.getResponseStatusLine(response));
        System.out.println("Response JSON from API: " + responseJson);

        actualStatusCode = BaseRequest.getResponseCode(response);
        actualStatusLine = BaseRequest.getResponseStatusLine(response);
        actualHeaderValue = BaseRequest.getResponseHeaderValue(response, "Content-Type");

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            actualId = (int) responseJson.get("id");
            actualName = String.valueOf(responseJson.get("name"));
            actualEmail = String.valueOf(responseJson.get("email"));
            actualGender = String.valueOf(responseJson.get("gender"));
            actualStatus = String.valueOf(responseJson.get("status"));
            expectedName = user.getName();
            expectedEmail = user.getEmail();
            expectedGender = user.getGender();
            expectedStatus = user.getStatus();

            response.then()
                    .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_200, "Response status code is not 200");
            Assert.assertTrue(actualStatusLine.contains("OK"), "Response reason phrase is not 'OK'");

            Assert.assertEquals(actualId, id, "Expected and actual partially updated user id do not match");
            Assert.assertEquals(actualName, expectedName, "Expected and actual partially updated user name do not match");
            Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual partially updated user email do not match");
            Assert.assertEquals(actualGender, expectedGender, "Expected and actual partially updated user gender do not match");
            Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual partially updated user status do not match");
        } else {
            actualMessage = String.valueOf(responseJson.get("message"));
            System.out.println("Requested user with ID " + id + " is already deleted");

            response.then()
                    .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("errorJsonSchema.json"));

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_404, "Response status code is not 404");
            Assert.assertTrue(actualStatusLine.contains("Not Found"), "Response reason phrase is not 'Not Found'");
            Assert.assertTrue(actualMessage.contains("not found"), "Expected and actual 'Not Found' message do not match");
        }
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        System.out.println("===================================================\n");
    }
}