package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {
    @Test
    public void longRedirect() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        System.out.println("Start with " + url);
        int code;
        do {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();
            url = response.getHeader("Location");
            if (url != null) {
                System.out.println(url);
            }
            code = response.getStatusCode();
        } while (code != 200);
    }
}