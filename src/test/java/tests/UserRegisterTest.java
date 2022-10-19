package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    public void testCreateUserWithIncorrectEmail() {
        String incorrectEmail = "incorrectEmail.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", incorrectEmail);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/registrationdata.csv")
    public void testCreateUserWithoutOneField(String email, String password, String username, String firstName, String lastName) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username", username);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);

    }

    @Test
    public void testCreateUserWithShortestName() {
        String shortestName = "Y";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", shortestName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    public void testCreateUserWithLongestName() {
        String longestName = "This username's name really is Daenerys Stormborn of House Targaryen, the First of Her Name, " +
                "Queen of the Andals and the First Men, Protector of the Seven Kingdoms, the Mother of Dragons, " +
                "the Khaleesi of the Great Grass Sea, the Unburnt, the Breaker of Chains";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", longestName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }

}
