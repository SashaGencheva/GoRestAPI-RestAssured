package api;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class DeleteRequest extends BaseRequest {
    public static Response delete(String url, HashMap<String, String> headerMap) {
        Response response = given()
                .headers(headerMap)
                .when()
                .delete(url);
        return response;
    }
}