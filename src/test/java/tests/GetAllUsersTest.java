package tests;

import api.BaseRequest;
import api.GetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Users;
import helper.Hooks;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GetAllUsersTest extends Hooks {
    File jsonData;
    ObjectMapper mapper;
    Users user;
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
    public void getAllUsersTest() throws IOException {

        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");
        //jsonData = new File("C:\\Users\\digger\\IdeaProjects\\GoRestAPIwithRestAssured\\src\\test\\java\\data\\users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class); //expected Users object

        //Call the API
        response = GetRequest.get(url, headerMap);
        response.then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usersJsonSchema.json"));
        //response.then().log().all();

        //Response to JSONArray
        JSONArray responseJson = new JSONArray(response.asString());
        System.out.println("================== Get All Users ==================");
        System.out.println("GET Request: " + url);

        System.out.println(BaseRequest.getResponseStatusLine(response));
        System.out.println("Response JSON from API: " + responseJson);


//        System.out.println(response.getStatusCode());
//        System.out.println(response.getStatusLine());
//        //System.out.println(response.print());
//        System.out.println(response.getHeader("Connection"));
//        System.out.println(response.getContentType());
//        System.out.println(response.asString());
//        System.out.println(response.asPrettyString());
        //System.out.println(response.jsonPath().getString("name"));
        //response.prettyPrint();

        actualStatusCode = BaseRequest.getResponseCode(response);
        actualStatusLine = BaseRequest.getResponseStatusLine(response);
        actualHeaderValue = BaseRequest.getResponseHeaderValue(response, "Content-Type");

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            actualId = (int) responseJson.getJSONObject(0).get("id");
            actualName = String.valueOf(responseJson.getJSONObject(0).get("name"));
            actualEmail = String.valueOf(responseJson.getJSONObject(0).get("email"));
            actualGender = String.valueOf(responseJson.getJSONObject(0).get("gender"));
            actualStatus = String.valueOf(responseJson.getJSONObject(0).get("status"));
            System.out.println("Last created user is: " + actualId + ", " + actualName + ", " + actualEmail + ", " + actualGender + ", " + actualStatus);

            expectedId = user.getId();
            expectedName = user.getName();
            expectedEmail = user.getEmail();
            expectedGender = user.getGender();
            expectedStatus = user.getStatus();

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_200, "Response status code is not 200");
            Assert.assertTrue(actualStatusLine.contains("OK"), "Response reason phrase is not 'OK'");

            Assert.assertEquals(actualId, expectedId, "Expected and actual created user id do not match in all users list");
            Assert.assertEquals(actualName, expectedName, "Expected and actual created user name do not match in all users list");
            Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual created user email do not match in all users list");
            Assert.assertEquals(actualGender, expectedGender, "Expected and actual created user gender do not match in all users list");
            Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual created user status do not match in all users list");
        } else {
            System.out.println("Last created user with ID " + user.getId() + " is already deleted");
        }
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        System.out.println("===================================================\n");
    }

    @Test
    public void getAllUsersParamsTest() throws IOException {
        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");
        //jsonData = new File("C:\\Users\\digger\\IdeaProjects\\GoRestAPIwithHTTPClient\\src\\test\\java\\data\\users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class); //expected Users object

        //Call the API
        response = GetRequest.getWithParams(url, accessToken);
        response.then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usersJsonSchema.json"));
        //response.then().log().all();

        //Response to JSONArray
        JSONArray responseJson = new JSONArray(response.asString());
        System.out.println("============ Get All Users With Params ============");

        System.out.println(BaseRequest.getResponseStatusLine(response));
        System.out.println("Response JSON from API: " + responseJson);

        actualStatusCode = BaseRequest.getResponseCode(response);
        actualStatusLine = BaseRequest.getResponseStatusLine(response);
        actualHeaderValue = BaseRequest.getResponseHeaderValue(response, "Content-Type");

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            actualId = (int) responseJson.getJSONObject(0).get("id");
            actualName = String.valueOf(responseJson.getJSONObject(0).get("name"));
            actualEmail = String.valueOf(responseJson.getJSONObject(0).get("email"));
            actualGender = String.valueOf(responseJson.getJSONObject(0).get("gender"));
            actualStatus = String.valueOf(responseJson.getJSONObject(0).get("status"));
            System.out.println("Last created user is: " + actualId + ", " + actualName + ", " + actualEmail + ", " + actualGender + ", " + actualStatus);

            expectedId = user.getId();
            expectedName = user.getName();
            expectedEmail = user.getEmail();
            expectedGender = user.getGender();
            expectedStatus = user.getStatus();

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_200, "Response status code is not 200");
            Assert.assertTrue(actualStatusLine.contains("OK"), "Response reason phrase is not 'OK'");

            Assert.assertEquals(actualId, expectedId, "Expected and actual created user id do not match in all users list");
            Assert.assertEquals(actualName, expectedName, "Expected and actual created user name do not match in all users list");
            Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual created user email do not match in all users list");
            Assert.assertEquals(actualGender, expectedGender, "Expected and actual created user gender do not match in all users list");
            Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual created user status do not match in all users list");
        } else {
            System.out.println("Last created user with ID " + user.getId() + " is already deleted");
        }
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        System.out.println("===================================================\n");
    }
}