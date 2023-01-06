import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import user.CreateUser;
import user.CreateUserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import user.RandomGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    private String emailYandex = randomGenerator.randomEmail() + "@yandex.ru";
    private String password = randomGenerator.randomPassword();
    private String name = randomGenerator.randomName();

    @Test
    @DisplayName("Create User")
    @Description("Проверяем создается ли новый пользователь")
    public void CreateUserTest(){
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));

        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);
    }

    @Test
    @DisplayName("Create User Again")
    @Description("Проверяем не создается ли уже существующий пользователь")
    public void CreateUserAgainTest(){
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));

        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse createUserAgainRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserAgainRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create User Null Fields Registration")
    @Description("Проверяем не создается ли пользователь c пустыми полями почты, пароля и имени")
    public void CreateUserNullFieldsTest(){
        ValidatableResponse createUserNullFieldEmailRequest = createUser.createUser(new CreateUserData("", password, name));
        createUserNullFieldEmailRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldPasswordRequest = createUser.createUser(new CreateUserData(emailYandex, "", name));
        createUserNullFieldPasswordRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldNameRequest = createUser.createUser(new CreateUserData(emailYandex, password, ""));
        createUserNullFieldNameRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));
    }

    //надо для создания пол реквеста


}
