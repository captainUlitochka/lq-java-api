import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HelloWorldTest {
    @Test
    public void testHelloWorld() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void testHelloWorldEx3() {
        System.out.println("Hello from Olga");
    }

    @Test
    public void testHelloWorldEx4() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.body().print();
    }

    @Test
    public void testRestAssured() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

         Response responseForGet = RestAssured
                 .given()
                 .body(data)
                 .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookies = new HashMap<>();
        if(responseCookie != null) {
        cookies.put("auth_cookie", responseCookie);}
        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();
    }

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
