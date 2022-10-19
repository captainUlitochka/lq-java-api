package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Data edit cases")
@Feature("Edit user's data")
@Story("As User I want to update my account's info")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("This test checks we can edit new user's we're authorized in data")
    @DisplayName("Edit new user's data")
    @Test
    public void testEditJustCreatedUser() {
        // Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        // Get
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Description("This test checks we can't edit user's data without authorization")
    @DisplayName("Edit user's data without authorization")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @Test
    public void testEditUserWithoutAuth() {
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/" + 2,
                        editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

    }

    @Description("This test checks we can't edit other user's data")
    @DisplayName("Edit other user's data")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @Test
    public void testEditOtherUser() {
        // Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        // Get 2nd user data
        JsonPath response2ndUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/23011", header, cookie).jsonPath();

        String otherName = response2ndUserData.getString("username");

        // Edit
        String newName = "homework";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + 23011,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseCodeEquals(responseEditUser, 400);

        // Get
        JsonPath check2ndUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/23011", header, cookie).jsonPath();

        System.out.println("вот такое имя после редактирования: " + check2ndUserData.prettyPrint());
        assertEquals(otherName, check2ndUserData.getString("username"));

    }

    @Description("This test checks we can't save invalid data for user")
    @DisplayName("Save invalid data")
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @ParameterizedTest
    @CsvFileSource(resources = "/invaliddata.csv")
    public void testEditToInvalidData(String type, String data) {
        // Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        // Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String cookie = responseGetAuth.getCookie("auth_sid");

        // Get user's data
        JsonPath responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie).jsonPath();

        String oldData = responseUserData.getString(type);

        // Edit
        Map<String, String> editData = new HashMap<>();
        editData.put(type, data);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseCodeEquals(responseEditUser, 400);

        // Check user's data
        JsonPath responseCheckUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie).jsonPath();

        assertEquals(oldData, responseCheckUserData.getString(type));

    }
}
