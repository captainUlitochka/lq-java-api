import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class TokenResult {
    public String token;
    public int seconds;

}
public class TokenTest {
    public static final String URL = "https://playground.learnqa.ru/ajax/api/longtime_job";

    public TokenResult getToken() {
        JsonPath response = RestAssured
                .get(URL)
                .jsonPath();
        response.prettyPrint();
        TokenResult tResult = new TokenResult();
        tResult.token = response.getString("token");
        tResult.seconds = response.getInt("seconds");
        return tResult;
    }

    public Map<String, String> checkJob(String token) {
        Map<String, String> answer = new HashMap<>();
        JsonPath checkResult = RestAssured
                .given()
                .queryParam("token", token)
                .get(URL)
                .jsonPath();
        String result = checkResult.getString("result");
        String status = checkResult.getString("status");

        answer.put("result", result);
        answer.put("status", status);
        return answer;
    }

   @Test
   public void getResult() throws InterruptedException {
       TokenResult tResult = getToken();
       Map<String, String> jobResult = checkJob(tResult.token);
       String status = jobResult.get("status");
       if (status.equals("Job is NOT ready")) {
           System.out.println(status);
           Thread.sleep(tResult.seconds * 1000);
           jobResult = checkJob(tResult.token);
           status = jobResult.get("status");
           Assertions.assertEquals("Job is ready", status);
       } else {
           Assertions.assertEquals("Job is ready", status);
       }
       System.out.println(jobResult.get("result"));
    }
}