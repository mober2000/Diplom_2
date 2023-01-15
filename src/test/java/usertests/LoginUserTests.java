package usertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import user.loginuser.LoginUser;
import org.junit.Test;

public class LoginUserTests {
    LoginUser loginUser = new LoginUser();
    String mail = "testtesttest1234@gmail.com";
    String password = "Test1234";
    String name = "TestTestTest";

    @Test
    @DisplayName("Login User")
    @Description("Проверка успешной авторизации пользователя")
    public void loginUser() {
        loginUser.loginUser(mail, password, name);
    }

    @Test
    @DisplayName("Login Unable User Field")
    @Description("Проверка авторизации пользователя с неверными логином и паролем")
    public void loginUnableFields() {
        loginUser.loginUnableFields(mail, password);
    }

    @Test
    @DisplayName("Login Null User Field")
    @Description("Проверка авторизации пользователя с пустыми полями логина и пароля")
    public void loginNullFields() {
        loginUser.loginNullFields(mail, password);
    }
}
