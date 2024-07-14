package apitests;

import api.pojo.AuthToken;
import api.step.UserStepHolder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static api.TestConstants.PASSWORD;

public class UserDataChangeTest extends AbstractTest {

    @DisplayName("User data change test for auth user")
    @Test
    public void userDataChangeWithAuthorizationSuccessTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        String accessToken = authTokens.getAccessToken();
        String refreshToken = authTokens.getRefreshToken();

        String newEmail = "new" + email;
        String newName = "new" + name;
        String newPassword = "new" + PASSWORD;


        JSONObject json = new JSONObject();
        json.put("email", newEmail);
        json.put("name", newName);
        json.put("password", newPassword);

        Response response = UserStepHolder.requestRenameSend(json.toString(), accessToken);
        UserStepHolder.responseSuccessCompare(response, newEmail, newName);
        UserStepHolder.logout(refreshToken);

        //Проверка GET, что пользователь действительно сохранился
        JSONObject getJson = new JSONObject();
        getJson.put("email", newEmail);
        getJson.put("name", newName);
        getJson.put("password", newPassword);

        AuthToken newAuthTokens = AuthToken.login(newEmail, newPassword);


        Assert.assertNotNull(newAuthTokens);
        Assert.assertNotNull(newAuthTokens.getAccessToken());

        String newAccessToken = newAuthTokens.getAccessToken();
        Response getResponse = UserStepHolder.getRequestSend(getJson.toString(), newAccessToken);
        UserStepHolder.responseGetCompare(getResponse, newName, newEmail);
        UserStepHolder.delete(newAccessToken);
    }

    @DisplayName("User data change test for NO auth user")
    @Test
    public void userDataChangeWithoutAuthorizationTest() {
        UserStepHolder.create(email, PASSWORD, name);
        String newName = "new" + name;
        String newEmail = "new" + email;
        String newPassword = "new" + PASSWORD;

        JSONObject json = new JSONObject();
        json.put("email", newEmail);
        json.put("name", newName);
        json.put("password", newPassword);

        Response response = UserStepHolder.requestRenameSend(json.toString(), "");
        UserStepHolder.responseNoAuthCompare(response);
    }
}


