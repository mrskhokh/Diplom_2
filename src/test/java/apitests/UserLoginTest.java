package apitests;

import api.TestUtils;
import api.pojo.AuthToken;
import api.step.UserStepHolder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;

import static api.TestConstants.PASSWORD;

public class UserLoginTest extends AbstractTest {

    @DisplayName("User login success test")
    @Test
    public void userLoginSuccessTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);

        Response response = UserStepHolder.requestLoginSend(json.toString());
        UserStepHolder.responseSuccessCheck(response, email, name);
        UserStepHolder.delete(authTokens.getAccessToken());
    }

    @DisplayName("User login wrong password test")
    @Test
    public void userLoginWrongPasswordTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD + TestUtils.generateRandomNumbers(3));

        Response response = UserStepHolder.requestLoginSend(json.toString());
        UserStepHolder.responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        UserStepHolder.delete(authTokens.getAccessToken());
    }

    @DisplayName("User login wrong email test")
    @Test
    public void userLoginWrongEmailTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);

        Response response = UserStepHolder.requestLoginSend(json.toString());
        UserStepHolder.responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        UserStepHolder.delete(authTokens.getAccessToken());
    }

}
