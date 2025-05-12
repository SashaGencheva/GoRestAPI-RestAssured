package api;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class GetRequest extends BaseRequest {
    public static Response get(String url, HashMap<String, String> headerMap) {
        Response response = given()
                .headers(headerMap)
                .when()
                .get(url);
        return response;
    }

    public static Response getWithParams(String url, String accessToken) {
        Response response = given()
                .queryParam("access-token", accessToken)
                .when()
                .get(url);
        return response;
    }
}