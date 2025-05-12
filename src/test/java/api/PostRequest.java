package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class PostRequest extends BaseRequest {
    public static Response post(String url, String payload, HashMap<String, String> headerMap) {
        Response response = given()
                .headers(headerMap)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(url);
        return response;
    }
}