import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserCreatingTest {
    public static final String PASSWORD = "12345678";
    public String name;
    public String email;

    @Before
    public void beforeEach() {
        name = "mrskhokh" + TestUtils.generateRandomNumbers(4);
        email = name + "@ya.ru";
    }

    @Step("Send POST request to https://stellarburgers.nomoreparties.site/api/auth/register ")
    public Response sentPostRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");
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

    @Test
    public void newUserCreatingSuccessTest() {
        String json = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + PASSWORD + "\",\n" +
                "\"name\": \"" + name + "\"\n" +
                "}";

        Response response = sentPostRequest(json);
        compareResponseFromRequest(response);
        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        User.delete(accessToken);
    }

    @Test
    public void doublicatedUserCreatingTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String json = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + PASSWORD + "\",\n" +
                "\"name\": \"" + name + "\"\n" +
                "}";
        Response response = sentPostRequest(json);
        compareResponseForDoublicatePostRequest(response);
        User.delete(authTokens.getAccessToken());
    }

    @Test
    public void userCreatingNoEmailTest() {
        String json = "{\n" +
                "\"email\": \"\",\n" +
                "\"password\": \"" + PASSWORD + "\",\n" +
                "\"name\": \"" + name + "\"\n" +
                "}";
        Response response = sentPostRequest(json);
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @Test
    public void userCreatingNoPasswordTest() {
        String json = "{\n" +
                "\"email\":\"" + email + "\",\n" +
                "\"password\": \"\",\n" +
                "\"name\": \"" + name + "\"\n" +
                "}";
        Response response = sentPostRequest(json);
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }

    @Test
    public void userCreatingNoNameTest() {
        String json = "{\n" +
                "\"email\":\"" + email + "\",\n" +
                "\"password\": \"" + PASSWORD + "\",\n" +
                "\"name\": \"\"\n" +
                "}";
        Response response = sentPostRequest(json);
        compareResponseForMistFieldsUserCreatePostRequest(response);
    }
}