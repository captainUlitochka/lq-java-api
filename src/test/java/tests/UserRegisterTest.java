package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
@Story("As User I want to create new account")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test checks we can't create user with duplicate email")
    @DisplayName("Create user with email which already exist")
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

    @Description("This test checks success scenario of new user's creation")
    @DisplayName("Create new user")
    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Description("This test checks we can't create user with email in incorrect format without @")
    @DisplayName("Create user with incorrect email")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
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

    @Description("This test checks we can't create user without one of fields")
    @DisplayName("Create user without one field")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
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

    @Description("This test checks we can't create user with one symbol name")
    @DisplayName("Create user with very short name")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
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

    @Description("This test checks we can't create user with name longer than 250 symbols")
    @DisplayName("Create user with very long name")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289481")
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
