package apiTests;

import apiTestUtils.AuthTokens;
import apiTestUtils.TestUtils;
import apiTestUtils.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static apiTestUtils.TestUtils.PASSWORD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends User {



    @Before
    public void beforeEach() {
        super.setUp();
    }

    @DisplayName("User login success test")
    @Test
    public void userLoginSuccessTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);
        Response response = requestLoginSend(json.toString());
        responseSuccessCheck(response);
        User.delete(authTokens.getAccessToken());
    }

    @DisplayName("User login wrong password test")
    @Test
    public void userLoginWrongPasswordTest() {

        AuthTokens authTokens = User.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD + TestUtils.generateRandomNumbers(3));
        Response response = requestLoginSend(json.toString());
        responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        User.delete(authTokens.getAccessToken());
    }

    @DisplayName("User login wrong email test")
    @Test
    public void userLoginWrongEmailTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);

        Response response = requestLoginSend(json.toString());
        responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        User.delete(authTokens.getAccessToken());
    }

}
