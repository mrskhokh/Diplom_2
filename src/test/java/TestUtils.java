import java.util.Random;

public final class TestUtils {

    public static String generateRandomNumbers(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            sb.append(random.nextInt(10)); // Генерируем случайное число от 0 до 9 и добавляем к строке
        }
        return sb.toString();
    }
}
