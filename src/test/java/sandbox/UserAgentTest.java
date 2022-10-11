package sandbox;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/useragent.csv")
    public void getUserAgent(String userAgent, String platform, String browser, String device) {

        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String actualPlatform = response.get("platform");
        String actualBrowser = response.get("browser");
        String actualDevice = response.get("device");

        response.prettyPrint();
        assertAll(
                () -> assertEquals(platform, actualPlatform),
                () -> assertEquals(browser, actualBrowser),
                () -> assertEquals(device, actualDevice));
    }

}
