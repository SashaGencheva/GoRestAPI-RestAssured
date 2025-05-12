package tests;

import api.BaseRequest;
import api.DeleteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Users;
import helper.Hooks;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DeleteUserTest extends Hooks {
    File jsonData;
    ObjectMapper mapper;
    Users user;
    HashMap<String, String> headerMap;
    Response response;

    int actualStatusCode;
    String actualStatusLine;
    String actualHeaderValue;
    String actualMessage;
    int id;

    @Test
    public void deleteUserTest(ITestContext context) throws IOException {
        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        //API Chaining
        id = (int) context.getSuite().getAttribute("userId");
        //id = 7774283;

        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class);

        //Call the API
        response = DeleteRequest.delete(url + "/" + id, headerMap);
        //response.then().log().all();

        System.out.println("=================== Delete User ===================");
        System.out.println("DELETE Request: " + url + "/" + id);
        System.out.println(BaseRequest.getResponseStatusLine(response));

        actualStatusCode = BaseRequest.getResponseCode(response);
        actualStatusLine = BaseRequest.getResponseStatusLine(response);

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            System.out.println("User with ID " + user.getId() + " is successfully deleted");

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_204, "Response status code is not 204");
            Assert.assertTrue(actualStatusLine.contains("No Content"), "Response reason phrase is not 'No Content'");
        } else {
            //JsonString to JSONObject
            JSONObject responseJson = new JSONObject(response.asString());
            actualMessage = String.valueOf(responseJson.get("message"));
            actualHeaderValue = BaseRequest.getResponseHeaderValue(response, "Content-Type");
            System.out.println("Requested user with ID " + user.getId() + " is already deleted");

            Assert.assertEquals(actualStatusCode, BaseRequest.RESPONSE_STATUS_CODE_404, "Response status code is not 404");
            Assert.assertTrue(actualStatusLine.contains("Not Found"), "Response reason phrase is not 'Not Found'");
            Assert.assertTrue(actualMessage.contains("not found"), "Expected and actual 'Not Found' message do not match");
            Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");
        }
        user.setName(null);
        user.setEmail(null);
        user.setGender(null);
        user.setStatus(null);
        mapper.writeValue(jsonData, user);

        System.out.println("===================================================\n");
    }
}