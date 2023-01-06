package usertests;

import universalclasses.RandomGenerator;
import useractions.GetUpdateAndDeleteUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import loginuser.LoginUser;
import loginuser.LoginUserData;
import org.junit.Test;
import usercreate.CreateUser;
import usercreate.CreateUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    LoginUser loginUser = new LoginUser();
    GetUpdateAndDeleteUser getUpdateAndDeleteUser =new GetUpdateAndDeleteUser();
    private final String emailYandex = randomGenerator.randomEmail() + "@yandex.ru";
    private final String password = randomGenerator.randomPassword();
    private final String name = randomGenerator.randomName();

    @Test
    @DisplayName("Login User")
    @Description("Проверка успешной авторизации пользователя")
    public void loginUserTest(){
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse loginUserRequest = loginUser.loginUser(new LoginUserData(emailYandex, password));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailLoginResponse = loginUserRequest.extract().path("user.email");
        String nameLoginResponse = loginUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailLoginResponse);
        assertEquals(name, nameLoginResponse);

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse deleteUserRequest = getUpdateAndDeleteUser.deleteUserKey(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Login Unable User Field")
    @Description("Проверка авторизации пользователя с неверными логином и паролем")
    public void loginUnableFiledsTest(){
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse loginUnableEmailFiledRequest = loginUser.loginUser(new LoginUserData("jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341", password));
        loginUnableEmailFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginUnablePasswordFiledRequest = loginUser.loginUser(new LoginUserData(emailYandex, "jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341"));
        loginUnablePasswordFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginUnableEmailAndPasswordFieldsRequest = loginUser.loginUser(new LoginUserData("jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341", "jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341"));
        loginUnableEmailAndPasswordFieldsRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse deleteUserRequest = getUpdateAndDeleteUser.deleteUserKey(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Test
    @DisplayName("Login Null User Field")
    @Description("Проверка авторизации пользователя с пустыми полями логина и пароля")
    public void loginNullFiledsTest(){
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse loginNullEmailFiledRequest = loginUser.loginUser(new LoginUserData("", password));
        loginNullEmailFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginNullPasswordFiledRequest = loginUser.loginUser(new LoginUserData(emailYandex, ""));
        loginNullPasswordFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginNullEmailAndPasswordFieldsRequest = loginUser.loginUser(new LoginUserData("", ""));
        loginNullEmailAndPasswordFieldsRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse deleteUserRequest = getUpdateAndDeleteUser.deleteUserKey(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
