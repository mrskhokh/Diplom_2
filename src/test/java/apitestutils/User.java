package apitestutils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class User extends AbstractTest {

    @Step("Send POST request to https://stellarburgers.nomoreparties.site/api/auth/register ")
    public static Response sentPostRegisterRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(Urls.apiUserRegister);
    }

    @Step("User creating")
    public static AuthTokens create(String email, String password, String name) {

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", password);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json.toString())
                        .when()
                        .post(Urls.apiUserRegister);

        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        String refreshToken = response.getBody().jsonPath().getString("refreshToken");

        return new AuthTokens(accessToken, refreshToken);
    }

    @Step("Logout")
    public static void logout(String refreshCode) {
        JSONObject json = new JSONObject();
        json.put("token", refreshCode);
        given()
                .body(json)
                .when()
                .post(Urls.apiLogout);
    }

    @Step("aieTestUtils.User deleting")
    public static void delete(String accessCode) {
        given()
                .auth().oauth2(accessCode)
                .when()
                .delete(Urls.apiUserDelete);

    }

    @Step("Get user data request send")
    public Response getRequestSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .get(Urls.apiUserData);
    }

    @Step("User data change PATCH request send")
    public Response requestRenameSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .patch(Urls.apiUserData);
    }

    @Step("Request sent")
    public Response requestLoginSend(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(Urls.apiLogin);
    }
    @Step("Compare response from POST request")
    public void compareResponseFromRequest(Response response) {
        response.then().assertThat().statusCode(200).and()
                .body("user.email", equalTo(email)).and()
                .body("user.name", equalTo(name));
    }

    @Step("Compare response for doublicated user POST request")
    public void compareResponseForDoublicatePostRequest(Response response) {
        response.then().assertThat().statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("User already exists"));
    }

    @Step("Compare responce with required fields missed in user creating POST request")
    public void compareResponseForMistFieldsUserCreatePostRequest(Response response) {
        response.then().assertThat().statusCode(403).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Step("Compare success response")
    public void responseSuccessCompare(Response response, String newEmail, String newName) {
        response.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));

    }

    @Step("Compare response with auth exception")
    public void responseNoAuthCompare(Response response) {
        response.then().assertThat().statusCode(401).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Compare success response")
    public void responseGetCompare(Response getResponse, String newName, String newEmail) {
        getResponse.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));
    }
    @Step("Response success check")
    public void responseSuccessCheck(Response response) {
        response.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("accessToken", notNullValue()).and()
                .body("user.email", equalTo(email)).and()
                .body("user.name", equalTo(name));
    }

    @Step("Response auth error check")
    public void responseAuthErrCheck(Response response, Integer code, Boolean success, String message) {
        response.then().assertThat().statusCode(code).and()
                .body("success", equalTo(success)).and()
                .body("message", equalTo(message));
    }

}