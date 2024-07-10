package apitests;

import apitestutils.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static apitestutils.TestUtils.PASSWORD;


public class UserCreatingTest extends User {
    @Before
    public void beforeEach() {
        super.setUp();
    }


    @DisplayName("New user creating success test")
    @Test
    public void newUserCreatingSuccessTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);

        Response response = User.sentPostRegisterRequest(json.toString());
        compareResponseFromRequest(response);
        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        User.delete(accessToken);
    }

    @DisplayName("Doublicated user creating")
    @Test
    public void doublicatedUserCreatingTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);
        Response response = User.sentPostRegisterRequest(json.toString());
        compareResponseForDoublicatePostRequest(response);
        User.delete(authTokens.getAccessToken());
    }

    @DisplayName("No email user creating test")
    @Test
    public void userCreatingNoEmailTest() {
        JSONObject json = new JSONObject();
        json.put("email", "");
        json.put("name", name);
        json.put("password", PASSWORD);
        Response response = User.sentPostRegisterRequest(json.toString());
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @DisplayName("No password user creating test")
    @Test
    public void userCreatingNoPasswordTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", "");
        Response response = User.sentPostRegisterRequest(json.toString());
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @Test
    @DisplayName("No name user creating test")
    public void userCreatingNoNameTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", "");
        json.put("password", PASSWORD);
        Response response = User.sentPostRegisterRequest(json.toString());
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }
}