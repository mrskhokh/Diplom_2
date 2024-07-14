package api;

import com.github.javafaker.Faker;

public final class TestUtils {

    public static String generateEmail() {
        return Faker.instance().bothify("????##@yandex.ru");
    }

    public static String generateUserName() {
        return Faker.instance().name().firstName();
    }

    public static String generateRandomNumbers(int length) {
        return String.valueOf(Faker.instance().number().digits(length));
    }
}
