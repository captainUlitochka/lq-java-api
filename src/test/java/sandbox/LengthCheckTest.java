package sandbox;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LengthCheckTest {
    @ParameterizedTest
    @ValueSource(strings = {"Ехал Грека через реку", "Привет"})
    public void shortPhraseTest(String condition) {

        assertTrue(condition.length() > 15, "Текст короче 15-ти символов");
    }
}
