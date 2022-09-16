import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class ParseTest {

    @Test
    public void testRestAssuredEx5() {
        List<HashMap<String, String>> values = RestAssured
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .then()
                .extract()
                .jsonPath()
                .getList("messages");
        String result = values.get(1).get("message");
        System.out.println(result);

    }
}
