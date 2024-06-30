import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {
    public static final String PASSWORD = "12345678";
    public String name;
    public String email;

    @Step("Request sent")
    public Response requestSend(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/login");
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

    @Before
    public void beforeEach() {
        name = "mrskhokh" + TestUtils.generateRandomNumbers(4);
        email = name + "@ya.ru";

    }

    @Test
    public void UserLoginSuccessTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String json = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + PASSWORD + "\"\n" +
                "}";
        Response response = requestSend(json);
        responseSuccessCheck(response);
        User.delete(authTokens.getAccessToken());
    }

    @Test
    public void UserLoginWrongPasswordTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String json = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + PASSWORD + TestUtils.generateRandomNumbers(2) + "\"\n" +
                "}";
        Response response = requestSend(json);
        responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        User.delete(authTokens.getAccessToken());
    }

    @Test
    public void UserLoginWrongEmailTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String json = "{\n" +
                "\"email\": \"" + email + TestUtils.generateRandomNumbers(2) + "\",\n" +
                "\"password\": \"" + PASSWORD + "\"\n" +
                "}";
        Response response = requestSend(json);
        responseAuthErrCheck(response, 401, false, "email or password are incorrect");
        User.delete(authTokens.getAccessToken());
    }

}
