package api.step;

import api.Urls;
import api.pojo.AuthToken;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserStepHolder {

    @Step("Send POST request to https://stellarburgers.nomoreparties.site/api/auth/register ")
    public static Response sentPostRegisterRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(Urls.API_USER_REGISTER);
    }

    @Step("User creating")
    public static AuthToken create(String email, String password, String name) {

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", password);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json.toString())
                        .when()
                        .post(Urls.API_USER_REGISTER);

        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        String refreshToken = response.getBody().jsonPath().getString("refreshToken");

        return new AuthToken(accessToken, refreshToken);
    }

    @Step("Logout")
    public static void logout(String refreshCode) {
        JSONObject json = new JSONObject();
        json.put("token", refreshCode);
        given()
                .body(json)
                .when()
                .post(Urls.API_LOGOUT);
    }

    @Step("aieTestUtils.User deleting")
    public static void delete(String accessCode) {
        given()
                .auth().oauth2(accessCode)
                .when()
                .delete(Urls.API_USER_DELETE);

    }

    @Step("Get user data request send")
    public static Response getRequestSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .get(Urls.API_USER_DATA);
    }

    @Step("User data change PATCH request send")
    public static Response requestRenameSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .patch(Urls.API_USER_DATA);
    }

    @Step("Request sent")
    public static Response requestLoginSend(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(Urls.API_LOGIN);
    }
    @Step("Compare response from POST request")
    public static void compareResponseFromRequest(Response response, String email, String name) {
        response.then().assertThat().statusCode(200).and()
                .body("user.email", equalTo(email)).and()
                .body("user.name", equalTo(name));
    }

    @Step("Compare response for doublicated user POST request")
    public static void compareResponseForDuplicatePostRequest(Response response) {
        response.then().assertThat().statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("User already exists"));
    }

    @Step("Compare response with required fields missed in user creating POST request")
    public static void compareResponseForMistFieldsUserCreatePostRequest(Response response) {
        response.then().assertThat().statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Step("Compare success response")
    public static void responseSuccessCompare(Response response, String newEmail, String newName) {
        response.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));

    }

    @Step("Compare response with auth exception")
    public static void responseNoAuthCompare(Response response) {
        response.then().assertThat().statusCode(401).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Compare success response")
    public static void responseGetCompare(Response getResponse, String newName, String newEmail) {
        getResponse.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));
    }
    @Step("Response success check")
    public static void responseSuccessCheck(Response response, String email, String name) {
        response.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("accessToken", notNullValue()).and()
                .body("user.email", equalTo(email)).and()
                .body("user.name", equalTo(name));
    }

    @Step("Response auth error check")
    public static void responseAuthErrCheck(Response response, Integer code, Boolean success, String message) {
        response.then().assertThat().statusCode(code).and()
                .body("success", equalTo(success)).and()
                .body("message", equalTo(message));
    }

}