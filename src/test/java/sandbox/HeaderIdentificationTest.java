package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderIdentificationTest {
    @Test
    public void getHeader() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String responseHeader = response.getHeader("x-secret-homework-header");
        System.out.println(responseHeader);
        assertEquals("Some secret value", responseHeader);
    }
}
