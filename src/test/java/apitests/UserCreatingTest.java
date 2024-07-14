package apitests;

import api.pojo.AuthToken;
import api.step.UserStepHolder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;

import static api.TestConstants.PASSWORD;

public class UserCreatingTest extends AbstractTest {
    @DisplayName("New user creating success test")
    @Test
    public void newUserCreatingSuccessTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);

        Response response = UserStepHolder.sentPostRegisterRequest(json.toString());
        UserStepHolder.compareResponseFromRequest(response, email, name);
        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        UserStepHolder.delete(accessToken);
    }

    @DisplayName("Doublicated user creating")
    @Test
    public void doublicatedUserCreatingTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", PASSWORD);

        Response response = UserStepHolder.sentPostRegisterRequest(json.toString());
        UserStepHolder.compareResponseForDuplicatePostRequest(response);
        UserStepHolder.delete(authTokens.getAccessToken());
    }

    @DisplayName("No email user creating test")
    @Test
    public void userCreatingNoEmailTest() {
        JSONObject json = new JSONObject();
        json.put("email", "");
        json.put("name", name);
        json.put("password", PASSWORD);

        Response response = UserStepHolder.sentPostRegisterRequest(json.toString());
        UserStepHolder.compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @DisplayName("No password user creating test")
    @Test
    public void userCreatingNoPasswordTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", name);
        json.put("password", "");

        Response response = UserStepHolder.sentPostRegisterRequest(json.toString());
        UserStepHolder.compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @Test
    @DisplayName("No name user creating test")
    public void userCreatingNoNameTest() {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("name", "");
        json.put("password", PASSWORD);

        Response response = UserStepHolder.sentPostRegisterRequest(json.toString());
        UserStepHolder.compareResponseForMistFieldsUserCreatePostRequest(response);
    }
}