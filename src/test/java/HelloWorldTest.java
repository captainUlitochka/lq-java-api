import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


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

         Response response = RestAssured
                 .given()
                 .redirects()
                 .follow(false)
                 .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
         response.prettyPrint();

         String locationHeader = response.getHeader("Location");
         System.out.println(locationHeader);
    }



}
