package usertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import testcases.CreateUser;
import testcases.LoginUser;
import org.junit.Test;
import testcases.UserActions;
import universalclasses.RandomGenerator;

public class LoginUserTests {
    LoginUser loginUser = new LoginUser();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    RandomGenerator randomGenerator = new RandomGenerator();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.setRandomPassword();
    String name = randomGenerator.setRandomName();

    @Test
    @DisplayName("Login User")
    @Description("Проверка успешной авторизации пользователя")
    public void loginUser() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        loginUser.loginUser(mail, password, name);
        userActions.deleteUser(bearerToken);

    }

    @Test
    @DisplayName("Login Unable User Field")
    @Description("Проверка авторизации пользователя с неверными логином и паролем")
    public void loginUnableFields() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        loginUser.loginUnableFields(mail, password);
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Login Null User Field")
    @Description("Проверка авторизации пользователя с пустыми полями логина и пароля")
    public void loginNullFields() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        loginUser.loginNullFields(mail, password);
        userActions.deleteUser(bearerToken);
    }
}
