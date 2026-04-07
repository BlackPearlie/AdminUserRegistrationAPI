package requestsBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static base.BaseURIs.baseURL;
import static payloads.PayloadBuilder.*;


public class ApiRequestBuilder {

    static String authToken;
    static String registeredUserId;

    public static Response loginUserResponse(String email, String password) {

        String apiPath = "/APIDEV/login";
        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .body(loginUserPayload(email, password))
                .log().all()
                .post()
                .then().extract().response();
        authToken = response.jsonPath().getString("data.token");
        return response;
    }

    public static Response registerUserResponse(String firstName, String lastName, String email, String password, String groupId) {

        String apiPath = "/APIDEV/register";
        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .body(registerUserPayload(firstName, lastName, email, password, groupId))
                .log().all()
                .post()
                .then().extract().response();
        registeredUserId = response.jsonPath().getString("data.id");
        return response;
    }

    public static Response approveUserRegistrationResponse() {

        String apiPath = "/APIDEV/admin/users/"+registeredUserId+"/approve";
        return RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .log().all()
                .put()
                .then().extract().response();
    }

    // update user to be admin user
    public static Response updateUserRoleResponse(String role) {

        String apiPath = "/APIDEV/admin/users/"+registeredUserId+"/role";
        return RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(approveRolePayload(role))
                .log().all()
                .put()
                .then().extract().response();
    }

    //get user role
    public static Response getUserResponse() {

        String apiPath = "/APIDEV/admin/users/"+registeredUserId;
        return RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .log().all()
                .get()
                .then().extract().response();
    }

    //delete user
    public static Response deleteUserResponse() {

        String apiPath = "/APIDEV/admin/users/"+registeredUserId;
        return RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .log().all()
                .delete()
                .then().extract().response();
    }

}
