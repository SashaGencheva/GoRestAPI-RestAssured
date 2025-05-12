package api;

import io.restassured.response.Response;

public class BaseRequest {
    public static final int RESPONSE_STATUS_CODE_200 = 200;
    public static final int RESPONSE_STATUS_CODE_201 = 201;
    public static final int RESPONSE_STATUS_CODE_204 = 204;
    public static final int RESPONSE_STATUS_CODE_404 = 404;

    /**
     * Custom method to get response status code
     *
     * @param response Method parameter of Response object type
     * @return It returns the status code of a Response object
     */
    public static int getResponseCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Custom method to get response status line
     *
     * @param response Method parameter of Response object type
     * @return It returns the status line of the response
     */
    public static String getResponseStatusLine(Response response) {
        return response.getStatusLine();
    }

    /**
     * Custom method to get value of specified header
     *
     * @param response   First method parameter of Response object type
     * @param headerName Second method parameter of String type, which is a specified header
     * @return It returns the value of a certain header
     */
    public static String getResponseHeaderValue(Response response, String headerName) {
        return response.getHeader(headerName);
    }
}