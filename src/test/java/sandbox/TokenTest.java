package sandbox;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {
    private static final String URL = "https://playground.learnqa.ru/ajax/api/longtime_job";
    private static final String JOB_READY = "Job is ready";
    private static final String JOB_PROCESSING = "Job is NOT ready";

    static class TokenResult {
        public String token;
        public long seconds;
    }

    static class JobResult {
        public String status;
        public String result;
    }
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
    public JobResult checkJob(String token) {
        JobResult jResult = new JobResult();
        JsonPath checkResult = RestAssured
                .given()
                .queryParam("token", token)
                .get(URL)
                .jsonPath();
        jResult.result = checkResult.getString("result");
        jResult.status = checkResult.getString("status");
        return jResult;
    }

   @Test
   public void getResult() throws InterruptedException {
       TokenResult tResult = getToken();
       JobResult jobResult = checkJob(tResult.token);
       if (jobResult.status.equals(JOB_PROCESSING)) {
           System.out.println(jobResult.status);
           Thread.sleep(tResult.seconds * 1000);
           jobResult = checkJob(tResult.token);
       }
       Assertions.assertEquals(JOB_READY, jobResult.status);
       System.out.println(jobResult.result);
    }
}