package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieIdentificationTest {

    @Test
    public void getCookie() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        String responseCookie = response.getCookie("HomeWork");
        assertEquals("hw_value", responseCookie);
    }
}